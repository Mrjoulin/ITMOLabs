package invoker.interfaces

import commands.interfaces.Command
import network.Request
import network.Response

/**
 * Interface of Invoker which describes the behavior of a invoker
 *
 * @author Matthew I.
 */
interface InvokerInterface {
    fun register(commandName: String, command: Command)
    fun execute(request: Request): Response
}