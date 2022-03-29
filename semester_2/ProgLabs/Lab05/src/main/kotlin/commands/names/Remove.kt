package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import receiver.interfaces.ReceiverInterface
import entities.Route
import utils.getEntityByIdFromInputArgs

import kotlin.collections.ArrayList

/**
 * Class to remove entity by given id from collection
 * Extends Command
 *
 * @param receiver Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "remove_by_id", args = "id", description = "Remove entity by given id from collection")
class Remove(private val receiver: ReceiverInterface<Route>) : Command {
    override fun execute(args: ArrayList<String>): Boolean {
        val result = getEntityByIdFromInputArgs(receiver.getEntitiesSet(), args)

        if (result.first == null) {
            println(result.second)
            return false
        }

        val route = result.first!!

        val success = receiver.removeEntity(route)

        if (success)
            println("Entity ID #${route.id} was removed")
        else
            println("Entity wasn't removed because of unexpected error!")

        return success
    }
}
