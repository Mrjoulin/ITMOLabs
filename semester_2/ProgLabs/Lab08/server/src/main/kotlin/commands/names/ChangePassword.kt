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
 * Class to update user password
 * Extends Command
 *
 * @param collectionManager Default command argument.
 *
 * @author Matthew I.
 */
@ConsoleCommand(name = "change_password", description = "Update user password")
class ChangePassword(private val collectionManager: CollectionManagerInterface<Route>) : Command(collectionManager) {
    override fun execute(request: Request): Response {
        if (request.commandArgs.size < 1)
            return Response(success = false, message = "You should pass one param for this command: new password")

        val user = getUser(request) ?: return USER_NOT_FOUND_RESPONSE
        val newPassword = request.commandArgs[0]

        val dbWorker = collectionManager.getDBWorker()

        // Check that user exist

        var success = dbWorker.updateUserPassword(userToken = user.token, newPassword = newPassword)

        if (!success) return DB_PROBLEM_RESPONSE

        // Generate new token for user and update in db

        val tokenExpires = java.sql.Date(Date().time + TOKEN_EXPIRES_MILS)
        val newToken = generateToken(user.username, tokenExpires.toString())

        val userWithNewToken = User(user.username, newPassword, newToken, tokenExpires)

        success = dbWorker.updateUserToken(userWithNewToken)

        return if (success) Response(success = true, message = newToken) else DB_PROBLEM_RESPONSE
    }
}
