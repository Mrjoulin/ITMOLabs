import network.PORT_ENV_NAME
import server.Server
import utils.SERVER_PORT
import utils.logger

import java.lang.Exception
import java.net.BindException
import kotlin.math.log
import kotlin.system.exitProcess


/**
 * Start server. Entry point
 *
 * @author Matthew I.
 */
class Main {
    fun start() {
        logger.info("Starting server...")

        val server = try {
            Server()
        } catch (e: BindException) {
            logger.error("Can't bind port $SERVER_PORT, try another port (using env variable $PORT_ENV_NAME)")
            exitProcess(2)
        }

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