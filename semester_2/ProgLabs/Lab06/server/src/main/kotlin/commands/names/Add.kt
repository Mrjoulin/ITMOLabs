package commands.names

import manager.interfaces.CollectionManagerInterface
import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import network.Request
import entities.Route
import network.Response


/**
 * Class to add new entity to collection
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "add", description = "Add new entity to collection")
class Add(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request) : Response {
        val newRouteStatus = getEntityFromRequest(request)

        if (newRouteStatus.first) {
            val newRoute = newRouteStatus.second as Route

            val success = collectionManager.addNewEntity(newRoute)

            return if (success)
                Response(
                    success = true,
                    message = "New entity ID #${newRoute.id} successfully added"
                )
            else
                Response(
                    success = true,
                    message = "Entity wasn't added because of unexpected error!"
                )
        }

        return newRouteStatus.second as Response
    }
}
