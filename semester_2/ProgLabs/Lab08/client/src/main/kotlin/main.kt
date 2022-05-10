import client.Authorization
import client.Client
import client.ClientSession
import utils.logger

import java.lang.Exception
import kotlin.system.exitProcess


/**
 * Start application. Entry point
 *
 * @author Matthew I.
 */
class Main {
    private val session = ClientSession()

    fun start() {
        logger.debug("Start console application")

        authorize()

        startProcess()
    }

    private fun authorize() {
        logger.debug("Start authorization")

        val success = Authorization(session).processAuth()

        if (!success) {
            logger.debug("Unsuccessful authorization, close client")
            exitProcess(2)
        }
    }

    private fun startProcess() {
        logger.debug("Start processing commands")

        val client = Client(session)

        println("Hello to the console application!")

        while (true) {
            println("To show available commands type: help")

            try {
                client.processCommands()
            } catch (e: Exception) {
                logger.debug("Error while processing client: {}", e)
            }
        }
    }
}


fun main() {
    Main().start()
}
