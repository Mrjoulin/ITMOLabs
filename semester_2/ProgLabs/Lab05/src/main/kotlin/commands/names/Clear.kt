package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import receiver.interfaces.ReceiverInterface
import entities.Route

import kotlin.collections.ArrayList

/**
 * Class to clear collection
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "clear", description = "Clear collection")
class Clear(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        receiver.setEntitiesSet(HashSet())

        println("Collection cleared")

        return true
    }
}
