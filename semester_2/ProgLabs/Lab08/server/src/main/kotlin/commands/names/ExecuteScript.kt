package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.Request
import network.Response
import network.USER_NOT_FOUND_RESPONSE


/**
 * Class to execute script in given file
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "execute_script", args = "filename", description = "Execute script in given file")
class ExecuteScript(val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        getUser(request) ?: return USER_NOT_FOUND_RESPONSE

        if (request.commandArgs.isEmpty()) {
            return Response(success = false, "Please input script filename to execute")
        }

        val filename = request.commandArgs[0]

        return Response(
            success = true,
            message = "Execute script from file $filename",
            scriptFileToProcess = filename
        )
    }
}