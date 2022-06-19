package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response

/**
 * Class to add new entity to collection if it lower than others
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "add_if_min", description = "Add new entity to collection if it lower than others")
class AddMin(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val newRouteStatus = getEntityFromRequest(request)

        if (newRouteStatus.first) {
            val newRoute = newRouteStatus.second as Route

            val minEntity = collectionManager.getEntitiesSet().minByOrNull { it }

            var success = true
            val message: String

            if (minEntity == null || newRoute < minEntity) {
                success = collectionManager.addNewEntity(newRoute)

                message = if (success)
                    "New entity ID #${newRoute.id} successfully added"
                else
                    "Entity wasn't added because of unexpected error!"
            } else
                message = "Entity isn't min in collection, so it wasn't added"

            return Response(success, message)
        }

        return newRouteStatus.second as Response
    }
}
