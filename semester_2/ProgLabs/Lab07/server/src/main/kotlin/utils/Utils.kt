package utils

import entities.validators.annotations.InputField
import entities.validators.annotations.UniqueId
import network.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.Integer.min
import java.security.SecureRandom
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


// Init logger

val logger: Logger = LoggerFactory.getLogger("server")

// Environment constants

val SERVER_PORT: Int
    get() {
        return if (System.getenv(PORT_ENV_NAME) != null)
            Integer.parseInt(System.getenv(PORT_ENV_NAME))
        else
            DEFAULT_SERVER_PORT
    }

val DATABASE_LOGIN: String
    get() = System.getenv(DATABASE_LOGIN_ENV)?.ifEmpty { DEFAULT_DATABASE_LOGIN } ?: DEFAULT_DATABASE_LOGIN

val DATABASE_PASSWORD: String
    get() = System.getenv(DATABASE_PASSWORD_ENV)?.ifEmpty { DEFAULT_DATABASE_PASSWORD } ?: DEFAULT_DATABASE_PASSWORD

val DATABASE_URL: String
    get() {
        return if (System.getenv(DATABASE_HOST_ENV) != null && System.getenv(DATABASE_NAME_ENV) != null)
            "jdbc:postgresql://${System.getenv(DATABASE_HOST_ENV)}/${System.getenv(DATABASE_NAME_ENV)}"
        else
            "jdbc:postgresql://${DEFAULT_DATABASE_HOST}/${DEFAULT_DATABASE_NAME}"
    }

// Constants

val NUM_THREADS_FOR_POOL: Int
    get() = Runtime.getRuntime().availableProcessors() / 3

const val DATABASE_ENTITIES_TABLE = "routes"
const val DATABASE_USERS_TABLE = "users"
const val DATABASE_IDS_SEQUENCE = "ids"


/**
 * Method to generate token with given args.
 * Creates Base64 encoded string of string "<args joined to string by ";">< Random string >" with length [DECODE_TOKEN_LENGTH]
 *
 * @param args vararg, String to add in decoded token
 *
 * @return Token encoded with Base64 algorithm
 *
 * @see Base64
 * @see SecureRandom
 *
 * @author Matthew I.
 */
fun generateToken(vararg args: String) : String {
    val secureRandom = SecureRandom()
    val base64Encoder: Base64.Encoder = Base64.getUrlEncoder()

    var secureString = (args.joinToString(";") + ";").toByteArray()

    if (secureString.size < DECODE_TOKEN_LENGTH) {
        val randomBytes = ByteArray(DECODE_TOKEN_LENGTH - secureString.size)
        secureRandom.nextBytes(randomBytes)
        secureString += randomBytes
    }

    return base64Encoder.encodeToString(secureString)
}

/**
 * Method to get hidden token, with shown [showFirst] characters, others replaced with * (max 10 stars)
 *
 * @param token Token witch need to be hidden
 * @param showFirst Number of symbols witch need to be shown
 *
 * @return Hidden token
 *
 * @author Matthew I.
 */
fun getHiddenToken(token: String, showFirst: Int = 20) : String {
    return token.take(showFirst) + "*".repeat(min(token.length - showFirst, 10))
}

/**
 * Get entity from HashSet of objects by given ID
 *
 * @param col HashSet of objects, one field in object must have annotation @UniqueId
 * @param args List of input command args, where to find id
 * @return Pair<Object, null> if success, else Pair<null, failure message>
 *
 * @author Matthew I.
 */
fun <T: Any> getEntityByIdFromInputArgs(col: HashSet<T>, args: ArrayList<String>) : Pair<T?, String?> {
    if (col.isEmpty())
        return Pair(null, "Collection is empty!")

    if (args.isEmpty())
        return Pair(null, "Please input id of entity for these command")
    if (args[0].toIntOrNull() == null)
        return Pair(null, "Id of entity must be Integer")

    return getEntityById(col, args[0].toInt())
}


/**
 * Get entity from HashSet of objects by given ID
 *
 * @param col HashSet of objects, one field in object must have annotation @UniqueId
 * @param id Id of object to find in collection
 * @return Pair<Object, null> if success, else Pair<null, failure message>
 *
 * @author Matthew I.
 */
fun <T: Any> getEntityById(col: HashSet<T>, id: Int) : Pair<T?, String?> {
    var fieldIdName: String? = null

    col.forEach { entity ->
        if (fieldIdName == null) {
            val field = entity.javaClass.declaredFields.find { it.isAnnotationPresent(UniqueId::class.java) }
            fieldIdName = field?.name ?: return Pair(null, "ID field (with annotation @UniqueId) not found in object")
        }
        try {
            val field = entity.javaClass.getDeclaredField(fieldIdName!!).apply { isAccessible = true }

            if (field.getInt(entity) == id) return Pair(entity, null)
        } catch (e: IllegalAccessException) {
            return Pair(null, "Can't get access to ID field!")
        }
    }

    return Pair(null, "There is no entities in collection with ID $id")
}

/**
 * Get object map, with fields names and values without nesting.
 * If field in object is annotated by @InputField and field type is not primitive, than
 * set to map "<field name>" + "<capitalize object field name in this field>": <value>
 * For example Coordinates -> {"coordinatesX": <value>, "coordinatesY": <value>}
 * Uses for get map for DB
 *
 * @see objectMap
 * @author Matthew I.
 */
@Suppress("UNCHECKED_CAST")
fun <T: Any> dbObjectMap(obj: T): Map<String, Any?> {
    val resultMap = objectMap(obj).toMutableMap()
    var changed = true

    while (changed) {
        changed = false

        for (fieldName in resultMap.keys) {
            val field = try {
                obj.javaClass.getDeclaredField(fieldName)
            } catch (e: NoSuchFieldException) { continue }

            if (field.isAnnotationPresent(InputField::class.java) &&
                    !field.getAnnotation(InputField::class.java).isPrimitiveType) {

                val deepMap = resultMap[fieldName] as Map<String, Any?>

                deepMap.forEach { (deepFieldName, value) ->
                    resultMap[fieldName + deepFieldName.capitalize()] = value
                    changed = true
                }

                resultMap.remove(fieldName)
                break
            }
        }
    }

    return resultMap
}

/**
 * Get object map with fields names and values.
 * If field in object is annotated by @InputField and field type is not primitive, recursive get map of field value
 *
 * @param obj Object witch from generate map
 * @return Map<String, Any?> with keys - fields names, values - fields values
 *
 * @author Matthew I.
 */
fun <T : Any> objectMap(obj: T) : Map<String, Any?> {
    val props = obj.javaClass.declaredFields.associateBy { it.name }

    return props.keys.associateWith {
        var value: Any? = null

        props[it]?.isAccessible = true

        if (props[it] != null && props[it]!!.isAnnotationPresent(InputField::class.java)) {
            val ann = props[it]!!.getAnnotation(InputField::class.java)
            if (!ann.isPrimitiveType)
                value = objectMap(props[it]!!.get(obj))
        }

        value ?: props[it]?.get(obj)
    }
}