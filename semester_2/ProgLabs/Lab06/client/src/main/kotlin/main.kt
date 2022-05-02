import utils.logger

import java.lang.Exception


/**
 * Start application. Entry point
 *
 * @author Matthew I.
 */
class Main {
    fun start() {
        logger.debug("Start console application")

        val client = Client()

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
