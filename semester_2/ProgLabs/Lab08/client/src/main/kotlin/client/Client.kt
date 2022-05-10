package client

import java.io.IOException
import java.io.InputStreamReader
import kotlin.system.exitProcess

import entities.Route
import input.getInput
import input.processScriptFile
import input.CreateEntityMap
import network.AUTHORIZATION_COMMANDS
import network.Request
import network.Response
import network.USER_NOT_FOUND_RESPONSE
import socket.SocketWorker
import utils.*

/**
 * Client process. Get input from user and send it to the server to process
 *
 * @param session Client session
 *
 * @see ClientSession
 * @see SocketWorker
 * @see getInput
 *
 * @author Matthew I.
 */
class Client(private val session: ClientSession) {
    private lateinit var currentCommand: String
    private lateinit var currentCommandArgs: ArrayList<String>

    fun processCommands(): Boolean {
        while (true) {
            try {
                // Get command with args from input
                val commandWithArgs = getInput(session.currentInput, "${session.username} $ ") ?: continue

                // Set current command and command args
                currentCommand = commandWithArgs.removeAt(0)
                currentCommandArgs = commandWithArgs

                // Check that input command exist
                val checkResponse = checkCommandRequest() ?: continue

                // Process check command response
                val success = processResponse(checkResponse)

                if (!success) return false
            }
            catch (e: IOException) {
                logger.debug("End of file received, go back")
                return true
            }
            catch (e: IllegalStateException) {
                println(e.message)
                return false
            }
        }
    }

    private fun processResponse(response: Response) : Boolean {
        if (!response.success) {
            if (response == USER_NOT_FOUND_RESPONSE) {
                println("You token has expired, please re-login")

                return Authorization(session).processAuth()
            }

            println(response.message.ifEmpty { "Unsuccessful request to the server!" })

            return false
        }

        if (response.isObjectNeeded) {
            val newResponse = sendObjectForCommand(response) ?: return false

            return processResponse(newResponse)
        }

        if (response.scriptFileToProcess != null)
            return processScriptFile(session, filename = response.scriptFileToProcess!!)

        if (response.message.isNotEmpty())
            println(response.message)

        return true
    }

    /**
     * Requests to the server process
     * */

    private fun checkCommandRequest(): Response? {
        if (currentCommand == "exit")
            exitClient()
        if (currentCommand in AUTHORIZATION_COMMANDS) {
            Authorization(session).authorizeByType(currentCommand.replace("_", " "))
            return null
        }

        val request = Request(
            token = session.userToken,
            command = currentCommand,
            commandArgs = currentCommandArgs
        )

        return session.socketWorker.makeRequest(request)
    }

    private fun sendObjectForCommand(commandResponse: Response): Response? {
        val routeToUpdateMap = objectMap(commandResponse.routeToUpdate)

        // Get all info from user
        val entityMap = CreateEntityMap(input = session.currentInput).getObjectMapFromInput(
            Route::class.java, routeToUpdateMap
        ) ?: return null

        // Sent object for these command to server
        val request = Request(
            token = session.userToken,
            command = currentCommand,
            commandArgs = currentCommandArgs,
            entityObjectMap = entityMap
        )

        return session.socketWorker.makeRequest(request)
    }

    /**
     * Disconnect from the server and exit client
     */
    private fun exitClient() {
        session.socketWorker.closeConnection()

        println("Exit program, come again!")

        exitProcess(0)
    }
}