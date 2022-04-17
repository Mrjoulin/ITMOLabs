package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response
import utils.getEntityByIdFromInputArgs


/**
 * Class to remove entity by given id from collection
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "remove_by_id", args = "id", description = "Remove entity by given id from collection")
class Remove(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val result = getEntityByIdFromInputArgs(collectionManager.getEntitiesSet(), request.commandArgs)

        if (result.first == null) {
            return Response(success = false, message = result.second!!)
        }

        val route = result.first!!

        val success = collectionManager.removeEntity(route)

        return Response(
            success = success,
            message =
                if (success)
                    "Entity ID #${route.id} was removed"
                else
                    "Entity wasn't removed because of unexpected error!"
        )
    }
}
