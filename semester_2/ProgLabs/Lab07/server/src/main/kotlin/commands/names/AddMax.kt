package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response


/**
 * Class to add new entity to collection if it greater than others
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "add_if_max", description = "Add new entity to collection if it greater than others")
class AddMax(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val newRouteStatus = getEntityFromRequest(request)

        if (newRouteStatus.first) {
            val newRoute = newRouteStatus.second as Route

            val maxEntity = collectionManager.getEntitiesSet().maxByOrNull { it }

            var success = true
            val message: String

            if (maxEntity == null || newRoute > maxEntity) {
                success = collectionManager.addNewEntity(newRoute)

                message = if (success)
                    "New entity ID #${newRoute.id} successfully added"
                else
                    "Entity wasn't added because of unexpected error!"
            } else
                message = "Entity isn't max in collection, so it wasn't added"

            return Response(success, message)
        }

        return newRouteStatus.second as Response
    }
}
