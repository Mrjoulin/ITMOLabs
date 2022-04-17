package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response
import utils.getEntityByIdFromInputArgs


/**
 * Class to update entity by given id
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "update", args = "id", description = "Update entity by given id")
class Update(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val result = getEntityByIdFromInputArgs(collectionManager.getEntitiesSet(), request.commandArgs)

        if (result.first == null)
            return Response(success = false, message = result.second!!)

        val route = result.first!!

        val updatedRouteStatus = getEntityFromRequest(request, previousObject = route)

        if (updatedRouteStatus.first) {
            val updatedRoute = updatedRouteStatus.second as Route

            var success = collectionManager.removeEntity(route)
            success = success && collectionManager.addNewEntity(updatedRoute)

            val message = if (success) "Entity ID ${route.id} successfully updated"
                          else "Entity wasn't updated! Unexpected error"

            return Response(success = success, message = message)
        }

        return updatedRouteStatus.second as Response
    }
}
