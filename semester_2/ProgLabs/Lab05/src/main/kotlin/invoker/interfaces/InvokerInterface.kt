package invoker.interfaces

import commands.interfaces.Command

/**
 * Interface of Invoker which describes the behavior of a invoker
 *
 * @author Matthew I.
 */
interface InvokerInterface {
    fun register(commandName: String, command: Command)
    fun execute(commandName: String, args: ArrayList<String>): Boolean
}