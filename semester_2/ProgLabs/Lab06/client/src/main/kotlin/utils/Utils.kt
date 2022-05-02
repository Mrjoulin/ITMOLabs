package utils

import java.net.InetAddress

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import entities.validators.annotations.InputField
import network.DEFAULT_SERVER_PORT
import network.HOST_EVN_NAME
import network.PORT_EVN_NAME


// Init logger
val logger: Logger = LoggerFactory.getLogger("client")

val SERVER_HOST: InetAddress
    get() {
        return if (System.getenv(HOST_EVN_NAME) != null)
            InetAddress.getByName(System.getenv(HOST_EVN_NAME))
        else
            InetAddress.getLocalHost()
    }
val SERVER_PORT: Int
    get() {
        return if (System.getenv(PORT_EVN_NAME) != null)
            Integer.parseInt(System.getenv(PORT_EVN_NAME))
        else
            DEFAULT_SERVER_PORT
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
fun <T : Any> objectMap(obj: T?) : Map<String, Any?>? {
    if (obj == null)
        return null

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