package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import network.*

/**
 * Class to get current user token
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "check_token", description = "Get current user token")
class CheckToken(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        val user = getUser(request) ?: return USER_NOT_FOUND_RESPONSE

        return Response(success = true, message = "Token for user ${user.username}:\n" + user.token)
    }
}
