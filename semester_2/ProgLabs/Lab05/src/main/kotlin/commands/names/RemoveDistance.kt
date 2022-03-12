package commands.names

import commands.interfaces.*
import entities.Route
import receiver.ReceiverInterface

import kotlin.collections.ArrayList

/**
 * Class to remove any entity from collection with distance equal given distance
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(
    name = "remove_any_by_distance", args = "distance",
    description = "Remove any entity from collection with distance equal given distance"
)
class RemoveDistance(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>) : Boolean {
        if (args.isEmpty()) {
            println("Please input distance of entity witch need to be removed")
            return false
        }

        val distance: Double? = args[0].toDoubleOrNull()

        if (distance == null) {
            println("Distance must be in double format!")
            return false
        }

        val route = receiver.getEntitiesSet().find { it.distance == distance }

        if (route != null) {
            val success = receiver.removeEntity(route)

            if (success)
                println("Entity ID #${route.id} with distance ${route.distance} was removed")
            else
                println("Entity found, but wasn't removed because of unexpected error")

            return success
        }

        println("Entity with distance $distance not found")

        return true
    }
}
