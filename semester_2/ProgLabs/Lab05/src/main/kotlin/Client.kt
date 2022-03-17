import file.CollectionFileProcess
import receiver.interfaces.ReceiverInterface
import receiver.RouteReceiver
import invoker.interfaces.InvokerInterface
import invoker.Invoker
import entities.Route
import utils.CommandsLoader
import utils.getInput
import utils.logger

import java.io.IOException
import kotlin.system.exitProcess

/**
 * Client process. Control Invoker and Receiver classes
 *
 * @param receiver If Receiver already exist it can be given as a param
 *
 * @author Matthew I.
 */
class Client(receiver: ReceiverInterface<Route>? = null) {
    private val receiver: ReceiverInterface<Route> = receiver ?: RouteReceiver()
    private val invoker: InvokerInterface = Invoker()

    fun loadCollectionFromFile() {
        logger.debug("Start processing collection file")
        val success = CollectionFileProcess(receiver).load()

        if (!success) {
            println("Exit application!")
            exitProcess(2)
        }
    }

    fun loadCommands() {
        logger.debug("Start registering commands")
        // Register commands
        CommandsLoader(receiver).getCommandsByNames().forEach {
            invoker.register(it.key, it.value)
        }
    }

    fun processCommands(): Boolean {
        while (true) {
            try {
                // Get command with args from input
                val commandWithArgs = getInput(receiver.getInputStreamReader(), "$ ") ?: continue
                val commandName = commandWithArgs.removeAt(0)

                // Execute given command
                val success = invoker.execute(commandName, commandWithArgs)

                if (!success) return false
            }
            catch (e: IOException) { return true }
            catch (e: IllegalStateException) { println(e.message); return false }
        }
    }
}