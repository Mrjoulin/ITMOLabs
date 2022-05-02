package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response

/**
 * Class to remove all entities from collection lower than given
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "remove_lower", description = "Remove all entities from collection lower than given")
class RemoveLower(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val routeStatus = getEntityFromRequest(request)

        if (routeStatus.first) {
            val route = routeStatus.second as Route

            val sortedEntities = collectionManager.getEntitiesSet().sorted()
            var cnt = 0

            sortedEntities.filter { it < route }.forEach {
                val success = collectionManager.removeEntity(it)
                cnt++

                if (!success)
                    return Response(false, "Entity ${it.id} wasn't removed because of unexpected error!")
            }

            return Response(
                success = true,
                message =
                    if (sortedEntities.isNotEmpty() || cnt > 0)
                        "All entities lower than given was removed. Removed $cnt entities"
                    else
                        "There is no entities in collection, so no entities removed"
            )
        }

        return routeStatus.second as Response
    }
}
