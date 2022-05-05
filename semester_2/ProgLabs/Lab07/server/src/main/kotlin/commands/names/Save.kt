package commands.names

import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response


/**
 * Class to save collection to the file (from environment)
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
// @ConsoleCommand(name = "save", description = "Save collection to the file (from environment)", canBeSentFromUser = false)
@Deprecated("This command no longer used in this project")
class Save(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        return Response(false, "This command no longer used in this project")
    }
}
