package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface

import kotlin.collections.ArrayList

/**
 * Class to show info about entity with max "to location"
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "max_by_to", description = "Show info about entity with max \"to location\"")
class ShowMaxByTo(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        val maxByTo = receiver.getEntitiesSet().maxByOrNull { it.to }

        println(maxByTo ?: "There is no entities added to the collection")

        return true
    }
}
