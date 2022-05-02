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
 * Class to Sign Up to another user
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "sign_up", description = "Sign Up to another user")
class SignUp(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        if (request.commandArgs.size < 2)
            return Response(success = false, message = "You should pass two params for this command: login and password")

        val username = request.commandArgs[0]
        val password = request.commandArgs[1]

        if (!CORRECT_LOGIN_RE.matcher(username).matches())
            return Response(success = false, message = "Username in incorrect format!")

        val dbWorker = collectionManager.getDBWorker()

        // Check that username not exist

        if (dbWorker.getUserByUsername(username) != null)
            return Response(success = false, message = "User with username $username already exist")

        // Create new user

        val tokenExpires = java.sql.Date(Date().time + TOKEN_EXPIRES_MILS)
        val token = generateToken(username, tokenExpires.toString())

        val user = User(username, password, token, tokenExpires)

        val success = dbWorker.addNewUser(user)

        return if (success) Response(success = true, message = token) else DB_PROBLEM_RESPONSE
    }
}
