package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response
import network.USER_NOT_FOUND_RESPONSE


/**
 * Class to show info about entities in collection
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "show", description = "Show info about entities in collection")
class Show(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        getUser(request) ?: return USER_NOT_FOUND_RESPONSE

        val col = collectionManager.getEntitiesSet().sortedBy { it.name }

        val message =
            if (col.isNotEmpty())
                col.joinToString(separator="\n\n", prefix="Info about all entities in collection:\n")
            else
                "There is no entities added to the collection"

        return Response(success = true, message = message)
    }
}
