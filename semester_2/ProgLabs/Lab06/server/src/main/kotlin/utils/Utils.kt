package utils

import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import entities.validators.annotations.InputField
import entities.validators.annotations.UniqueId
import network.DEFAULT_SERVER_PORT
import network.COLLECTION_ENV_NAME
import network.PORT_EVN_NAME


// Init logger
val logger: Logger = LoggerFactory.getLogger("server")

val COLLECTION_FILENAME: String? = System.getenv(COLLECTION_ENV_NAME)

val SERVER_PORT: Int
    get() {
        return if (System.getenv(PORT_EVN_NAME) != null)
            Integer.parseInt(System.getenv(PORT_EVN_NAME))
        else
            DEFAULT_SERVER_PORT
    }


/**
 * Check collection file (from environment variable) is exist and in correct format
 *
 * @return Pair<success, message or null>. If all is correct message is null,
 *         else message contains information about the error
 *
 * @author Matthew I.
 */
fun checkCollectionFile() : Pair<Boolean, String?> {
    if (COLLECTION_FILENAME == null || COLLECTION_FILENAME.isEmpty())
        return Pair(false, "Environment variable $COLLECTION_ENV_NAME with file of collection not found!")
    if (!COLLECTION_FILENAME.endsWith(".xml"))
        Pair(false, "File of collection must be on xml format!")
    if (!File(COLLECTION_FILENAME).exists())
        return Pair(
            false,
            "File of collection isn't exist! Check path and filename in environment variable $COLLECTION_ENV_NAME"
        )

    return Pair(true, null)
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
 * Get object map with fields names and values
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