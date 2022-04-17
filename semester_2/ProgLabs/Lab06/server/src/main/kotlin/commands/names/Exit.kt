package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response


/**
 * Class to exit program (without saving)
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "exit", description = "Exit application")
class Exit(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        return Response(
            success = false,
            message = "The exit command could not be executed on the server"
        )
    }
}
