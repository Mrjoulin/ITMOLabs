package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response
import network.USER_NOT_FOUND_RESPONSE


/**
 * Class to show info about collection
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "info", description = "Show info about collection")
class Info(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        getUser(request) ?: return USER_NOT_FOUND_RESPONSE

        val col = collectionManager.getEntitiesSet()

        val message = "Collection of entities info:\n" +
                "- Collection type: ${col.javaClass.simpleName}\n" +
                "- Initialization date: ${collectionManager.getCreationDate()}\n" +
                "- Elements type: ${Route::class.java.simpleName}\n" +
                "- Elements count: ${col.size}"

        return Response(success = true, message = message)
    }
}
