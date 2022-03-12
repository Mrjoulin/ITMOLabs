package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface

import kotlin.collections.ArrayList

/**
 * Class to show info about entities in collection
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "show", description = "Show info about entities in collection")
class Show(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val col = receiver.getEntitiesSet().sortedBy { it.id }

        println(
            if (col.isNotEmpty())
                col.joinToString(separator="\n\n", prefix="Info about all entities in collection:\n")
            else "There is no entities added to the collection"
        )

        return true
    }
}
