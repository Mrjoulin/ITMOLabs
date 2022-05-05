package server

import entities.Route
import invoker.Invoker
import invoker.interfaces.InvokerInterface
import manager.CollectionManager
import manager.interfaces.CollectionManagerInterface
import network.Request
import network.Response
import socket.SocketWorker
import socket.interfaces.SocketWorkerInterface
import utils.CommandsLoader
import utils.NUM_THREADS_FOR_POOL
import utils.getInput
import utils.logger
import java.io.InputStreamReader
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.system.exitProcess


/**
 * Main class to process server application.
 * server.Server receive requests from client or from the console and execute commands, that they sent
 * On creating server object, collection is loading from file and commands (loaded by CommandsLoaders) registering in invoker
 *
 * @see CollectionManager
 * @see Invoker
 * @see SocketWorker
 * @see CommandsLoader
 * @see ThreadPoolExecutor
 *
 * @author Matthew I.
 */
class Server {
    private val collectionManager: CollectionManagerInterface<Route> = CollectionManager()
    private val userInvoker: InvokerInterface = Invoker()
    private val specialInvoker: InvokerInterface = Invoker()
    private val socketWorker: SocketWorkerInterface = SocketWorker()
    private val threadPoolExecutor = Executors.newFixedThreadPool(NUM_THREADS_FOR_POOL) as ThreadPoolExecutor

    init {
        loadCollectionFromDataBase()
        loadCommands()
    }

    private fun loadCollectionFromDataBase() {
        logger.info("Start loading collection from db")

        val success = collectionManager.loadEntitiesSet()

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
            val request = socketWorker.getUpdates()

            threadPoolExecutor.submit(
                ProcessRequest(userInvoker, socketWorker, request)
            )
        }
    }

    fun processSpecialCommands() {
        val input = InputStreamReader(System.`in`)

        while (true) {
            // Get command with args from input
            val commandWithArgs = getInput(input) ?: continue
            val commandName = commandWithArgs.removeAt(0)

            if (commandName == "exit") exitServer()

            val request = Request(command = commandName, commandArgs = commandWithArgs)

            threadPoolExecutor.submit(
                ProcessRequest(specialInvoker, null, request)
            )
        }
    }

    private fun exitServer() {
        socketWorker.closeSocket()

        logger.info("Exit server!")

        exitProcess(0)
    }
}