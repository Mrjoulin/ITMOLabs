package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response


/**
 * Class to clear collection
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "clear", description = "Clear collection")
class Clear(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        collectionManager.setEntitiesSet(HashSet())

        return Response(success = true, message = "Collection cleared")
    }
}
