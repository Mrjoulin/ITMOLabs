import utils.logger

import java.lang.Exception


/**
 * Start server. Entry point
 *
 * @author Matthew I.
 */
class Main {
    fun start() {
        logger.info("Starting server...")

        val server = Server()

        logger.info("Start processing console input (for special commands)")
        Thread(server::processSpecialCommands).start()

        while (true) {
            logger.info("Server is running")

            try {
                server.startServer()
            } catch (e: Exception) {
                logger.error("Error while processing server: {}", e)
            }
        }
    }
}


fun main() {
    Main().start()
}