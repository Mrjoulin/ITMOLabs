package network

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Request to the server object.
 * Implements Serializable interface
 *
 * @param command Command name
 * @param commandArgs Args for this command
 * @param entityObjectMap Map with all info needed from user to create entity
 *
 * @see Response
 *
 * @author Matthew I.
 */
class Request (
    val command : String,
    val commandArgs: ArrayList<String>,
    val entityObjectMap: Map<String, Any?>? = null
) : Serializable {

    override fun toString(): String {
        return "{" +
                "command = $command; " +
                "commandArgs: ${commandArgs.joinToString(prefix = "[", postfix = "]")}; " +
                "entityMap: ${entityObjectMap.toString()}" +
                "}"
    }

    override fun hashCode(): Int {
        return Objects.hash(command, commandArgs, entityObjectMap)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Request

        if (command != other.command) return false
        if (commandArgs != other.commandArgs) return false
        if (entityObjectMap != other.entityObjectMap) return false

        return true
    }
}