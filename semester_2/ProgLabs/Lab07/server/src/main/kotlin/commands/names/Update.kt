package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.HAS_NO_ACCESS_RESPONSE
import network.USER_NOT_FOUND_RESPONSE
import network.Request
import network.Response
import utils.getEntityByIdFromInputArgs
import utils.logger


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
        val user = getUser(request) ?: return USER_NOT_FOUND_RESPONSE

        val result = getEntityByIdFromInputArgs(collectionManager.getEntitiesSet(), request.commandArgs)

        if (result.first == null)
            return Response(success = false, message = result.second!!)

        val route = result.first!!

        if (route.author != user.username) return HAS_NO_ACCESS_RESPONSE

        val updatedRouteStatus = getEntityFromRequest(request = request, previousObject = route, user = user)

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
