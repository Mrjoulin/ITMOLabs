package network

import entities.Route
import java.io.Serializable
import java.util.*


/**
 * Response from the server object.
 * Implements Serializable interface
 *
 * @param success Success of the operation
 * @param message Message about operation
 * @param isObjectNeeded Flag that shows, is user need to input entity object from this command
 * @param scriptFileToProcess Filename or null, script file to process on client
 * @param routeToUpdate Route object or null, is needed if user wants to update some info obout this object
 *
 * @see Request
 *
 * @author Matthew I.
 */
class Response(
    val success: Boolean,
    val message: String,
    val isObjectNeeded: Boolean = false,
    val scriptFileToProcess: String? = null,
    val routeToUpdate: Route? = null
): Serializable {
    override fun toString(): String {
        return "{" +
                "success: $success; " +
                "message: ${if (message.length > 30) message.take(30) + "..." else message}; " +
                "isObjectNeeded: $isObjectNeeded; " +
                "scriptFileToProcess: $scriptFileToProcess; " +
                "haveRouteToUpdate: ${routeToUpdate != null}" +
                "}"
    }

    override fun hashCode(): Int {
        return Objects.hash(success, message, isObjectNeeded, scriptFileToProcess, routeToUpdate)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (success != other.success) return false
        if (message != other.message) return false
        if (isObjectNeeded != other.isObjectNeeded) return false
        if (scriptFileToProcess != other.scriptFileToProcess) return false
        if (routeToUpdate != other.routeToUpdate) return false

        return true
    }
}