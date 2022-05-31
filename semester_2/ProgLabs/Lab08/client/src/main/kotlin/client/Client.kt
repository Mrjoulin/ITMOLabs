package client

import authorization.Authorization
import java.io.IOException
import kotlin.system.exitProcess

import entities.Route
import entities.validators.exceptions.IncorrectFieldDataException
import input.getInput
import input.processScriptFile
import input.CreateEntityMap
import network.AUTHORIZATION_COMMANDS
import network.Request
import network.Response
import network.USER_NOT_FOUND_RESPONSE
import socket.SocketWorker
import utils.*
import java.lang.Exception

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
    private var successChanged = true
    private var currentSuccess = true

    fun processCommands(): Boolean {
        while (true) {
            try {
                while (!successChanged) { Thread.sleep(10) }
                if (!currentSuccess) return false

                // Get command with args from input
                val commandWithArgs = getInput(session.currentInput) ?: continue

                // Set current command and command args
                currentCommand = commandWithArgs.removeAt(0)
                currentCommandArgs = commandWithArgs

                // Check that input command exist
                checkCommandRequest()
            }
            catch (e: IOException) {
                logger.debug("End of file received, go back")
                return true
            }
            catch (e: IllegalStateException) {
                session.currentOutput.println(e.message)
                return false
            } catch (e: Exception) {
                return false
            }
        }
    }

    private fun processResponse(response: Response) : Boolean {
        if (!response.success) {
            if (response == USER_NOT_FOUND_RESPONSE) {
                session.currentOutput.println("You token has expired, please re-login")
                session.userToken = ""

                return false // TODO re-login
            }

            session.currentOutput.println(response.message.ifEmpty { "Unsuccessful request to the server!" })

            return false
        }

        if (response.isObjectNeeded) {
            val request = getRequestWithObjectForCommand(response) ?: return false

            session.socketWorker.makeAsyncRequest(request, { session.currentOutput.println(it.message) }) {
                processResponse(it)
            }

            return currentSuccess
        }

        if (response.scriptFileToProcess != null)
            return processScriptFile(session, filename = response.scriptFileToProcess!!)

        if (response.message.isNotEmpty())
            session.currentOutput.println(response.message)

        return true
    }

    /**
     * Requests to the server process
     * */

    private fun checkCommandRequest() {
        if (currentCommand == "exit")
            // Pass command
            return
        if (currentCommand in AUTHORIZATION_COMMANDS) {
            Authorization(session).authorizeByType(currentCommand.replace("_", " "))
            return
        }

        val request = Request(
            token = session.userToken,
            command = currentCommand,
            commandArgs = currentCommandArgs
        )

        session.socketWorker.makeAsyncRequest(request, {}) {
            // Process check command response
            val success = processResponse(it)

            currentSuccess = success
        }
    }

    private fun getRequestWithObjectForCommand(commandResponse: Response): Request? {
        val routeToUpdateMap = objectMap(commandResponse.routeToUpdate)

        // Get all info from user
        try {
            val entityMap = CreateEntityMap(input = session.currentInput).getObjectMapFromInput(
                Route::class.java, routeToUpdateMap
            )

            // Sent object for these command to server

            return Request(
                token = session.userToken,
                command = currentCommand,
                commandArgs = currentCommandArgs,
                entityObjectMap = entityMap
            )
        } catch (e: IncorrectFieldDataException) {
            session.currentOutput.println(e.message)
            return null
        }
    }

    /**
     * Disconnect from the server and exit client
     */
    private fun exitClient() {
        session.socketWorker.closeConnection()

        session.currentOutput.println("Exit program, come again!")

        exitProcess(0)
    }
}