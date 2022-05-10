package commands.names

import commands.annotations.ConsoleCommand
import commands.interfaces.Command
import manager.interfaces.CollectionManagerInterface
import entities.Route
import entities.User
import network.*
import utils.generateToken
import java.util.*


/**
 * Class to re-login to another user
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "login", description = "Re-Login to another user")
class Login(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        if (request.commandArgs.size < 2)
            return Response(success = false, message = "You should pass two params for this command: login and password")

        val username = request.commandArgs[0]
        val password = request.commandArgs[1]

        if (!CORRECT_LOGIN_RE.matcher(username).matches())
            return Response(success = false, message = "Username in incorrect format!")

        val dbWorker = collectionManager.getDBWorker()

        // Check that user exist

        dbWorker.getUserByUsername(username, password) ?: return INVALID_USER_CREDENTIALS_RESPONSE

        // Generate new token for user and update in db

        val tokenExpires = java.sql.Date(Date().time + TOKEN_EXPIRES_MILS)
        val newToken = generateToken(username, tokenExpires.toString())

        val user = User(username, password, newToken, tokenExpires)

        val success = dbWorker.updateUserToken(user)

        return if (success) Response(success = true, message = newToken) else DB_PROBLEM_RESPONSE
    }
}
