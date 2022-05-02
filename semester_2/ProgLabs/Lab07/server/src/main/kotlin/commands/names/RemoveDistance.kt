package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response
import network.USER_NOT_FOUND_RESPONSE


/**
 * Class to remove any entity from collection with distance equal given distance
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(
    name = "remove_any_by_distance", args = "distance",
    description = "Remove any entity from collection with distance equal given distance"
)
class RemoveDistance(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val user = getUser(request) ?: return USER_NOT_FOUND_RESPONSE

        if (request.commandArgs.isEmpty())
            return Response(success = false, message = "Please input distance of entity witch need to be removed")

        // Get distance from args
        val distance: Double = request.commandArgs[0].toDoubleOrNull()
            ?: return Response(false, "Distance must be in double format!")

        // Find entity with given distance
        val route = collectionManager.getEntitiesSet().find { it.distance == distance && it.author == user.username }

        if (route != null) {
            val success = collectionManager.removeEntity(route)

            return Response(
                success = success,
                message =
                    if (success)
                        "Entity ID #${route.id} with distance ${route.distance} was removed"
                    else
                        "Entity found, but wasn't removed because of unexpected error"
            )
        }

        return Response(success = true, message = "Entity with distance $distance not found")
    }
}
