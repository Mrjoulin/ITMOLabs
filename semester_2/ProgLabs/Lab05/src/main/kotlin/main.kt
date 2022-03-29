import utils.logger

import java.lang.Exception


/**
 * Start application. Entry point
 *
 * @author Matthew I.
 */
class Main {
    fun start() {
        logger.info("Start application, load client")

        val client = Client()

        // Load collection from environment file
        client.loadCollectionFromFile()
        // Load commands to invoker
        client.loadCommands()

        println("Hello to the console application!")

        while (true) {
            println("To show available commands type: help")

            try {
                client.processCommands()
            } catch (e: Exception) {
                logger.error("Error while processing client: {}", e)
            }
        }
    }
}


fun main() {
    Main().start()
}
