import kotlin.system.exitProcess

import manager.interfaces.CollectionManagerInterface
import manager.CollectionManager
import invoker.interfaces.InvokerInterface
import invoker.Invoker
import socket.interfaces.SocketWorkerInterface
import socket.SocketWorker
import file.CollectionFileProcess
import entities.Route
import network.Request
import network.Response
import utils.CommandsLoader
import utils.getInput
import utils.logger
import java.io.InputStreamReader


/**
 * Main class to process server application.
 * Server receive requests from client or from the console and execute commands, that they sent
 * On creating server object, collection is loading from file and commands (loaded by CommandsLoaders) registering in invoker
 *
 * @see CollectionManager
 * @see Invoker
 * @see SocketWorker
 * @see CollectionFileProcess
 * @see CommandsLoader
 *
 * @author Matthew I.
 */
class Server {
    private val collectionManager: CollectionManagerInterface<Route> = CollectionManager()
    private val userInvoker: InvokerInterface = Invoker()
    private val specialInvoker: InvokerInterface = Invoker()
    private val socketWorker: SocketWorkerInterface = SocketWorker()
    private var collectionFileProcess = CollectionFileProcess(collectionManager)

    init {
        loadCollectionFromFile()
        loadCommands()
    }

    private fun loadCollectionFromFile() {
        logger.info("Start processing collection file")

        val success = collectionFileProcess.load()

        if (!success) {
            logger.error("Exit application!")
            exitProcess(2)
        }
    }

    private fun loadCommands(){
        val commandsLoader = CommandsLoader(collectionManager)

        logger.info("Start registering commands for user invoker")

        commandsLoader.getUserCommandsByNames().forEach {
            userInvoker.register(it.key, it.value)
        }

        logger.info("Start registering commands for special commands invoker")

        commandsLoader.getSpecialCommandsByNames().forEach {
            specialInvoker.register(it.key, it.value)
        }
    }

    fun startServer() {
        while (true) {
            val newRequest = socketWorker.getUpdates()

            val response = try {
                userInvoker.execute(newRequest)
            } catch (e: IllegalStateException) {
                logger.info(e.message)
                Response(success = false, message = e.message ?: "Illegal State")
            }

            socketWorker.sendResponse(response)

            saveCollectionIfChanged()
        }
    }

    fun processSpecialCommands() {
        val input = InputStreamReader(System.`in`)

        while (true) {
            // Get command with args from input
            val commandWithArgs = getInput(input) ?: continue
            val commandName = commandWithArgs.removeAt(0)

            if (commandName == "exit")
                exitServer()

            val request = Request(command = commandName, commandArgs = commandWithArgs)

            try {
                val response = specialInvoker.execute(request)

                println(response.message)

                saveCollectionIfChanged()
            } catch (e: IllegalStateException) {
                println(e.message)
            } catch (e: Exception) {
                logger.error("Error while processing command $commandName: ", e)
            }
        }
    }


    private fun saveCollectionIfChanged() {
        if (collectionManager.isCollectionChanged()) {
            val success = collectionFileProcess.save()

            if (success)
                collectionManager.setCollectionChanged(false)
            else
                logger.error("Can't save collection to file!")
        }
    }

    private fun exitServer() {
        socketWorker.closeSocket()

        logger.info("Exit server!")

        exitProcess(0)
    }
}