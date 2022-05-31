package socket

import client.CollectionManager
import javafx.application.Platform
import javafx.util.Callback
import network.*
import network.enums.UpdateListenerRequestType
import network.enums.UpdateType
import utils.exceptions.UnsuccessfulRequestException
import socket.interfaces.SocketWorkerInterface
import utils.SERVER_HOST
import utils.SERVER_PORT
import utils.logger
import java.io.IOException
import java.io.Serializable
import java.lang.ClassCastException
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.util.function.Consumer
import kotlin.jvm.Throws
import kotlin.math.log


class SocketWorker(private val entitiesCollection: CollectionManager) : SocketWorkerInterface {
    private val socketAddress: SocketAddress = InetSocketAddress(SERVER_HOST, SERVER_PORT)
    private val datagramChannel: DatagramChannel = DatagramChannel.open()
    private val datagramListenerChannel: DatagramChannel = DatagramChannel.open()
    private var currentChannel: DatagramChannel = datagramChannel

    init {
        connectToServer()
    }

    override fun connectToServer() {
        try {
            logger.debug("Connect to server $socketAddress")

            datagramChannel.configureBlocking(false)
            datagramChannel.connect(socketAddress)

            datagramListenerChannel.configureBlocking(false)
            datagramListenerChannel.connect(socketAddress)
        } catch (e: IOException) {
            println("Some problems with network, can't connect to server!")
        }
    }

    override fun closeConnection() {
        if (datagramChannel.isConnected) {
            logger.debug("Disconnect from server $socketAddress")

            datagramChannel.disconnect()
        }

        if (datagramListenerChannel.isConnected) {
            logger.debug("Disconnect server updates listener")

            datagramListenerChannel.disconnect()
        }
    }

    @Synchronized override fun makeAsyncRequest(request: Serializable, errorHandler: Consumer<Exception>, callback: Consumer<Response>) {
        val thread = Thread { makeRequest(request, errorHandler, callback) }
        thread.isDaemon = true
        thread.start()
    }

    @Throws(UnsuccessfulRequestException::class)
    @Synchronized private fun makeRequest(request: Serializable, errorHandler: Consumer<Exception>, callback: Consumer<Response>) {
        val dataToSend = ObjectSerializer().toByteArray(request)

        if (!currentChannel.isConnected)
            connectToServer()

        // Clear data from channel if exist
        synchronized(currentChannel) {
            try {
                val clearBuffer = ByteBuffer.allocate(DEFAULT_PACKAGE_SIZE)
                while (currentChannel.read(clearBuffer) != 0) {
                    logger.info("Cleared ${clearBuffer.position()} bytes from channel")
                    clearBuffer.clear()
                }
            } catch (ignored: IOException) { }

            try {
                logger.debug("Send request to the server, request size: ${dataToSend.size}")

                val buf: ByteBuffer = ByteBuffer.wrap(dataToSend)

                do {
                    currentChannel.write(buf)
                } while (buf.hasRemaining())

                receiveAnswer(errorHandler, callback)
            } catch (exception: IOException) {
                Platform.runLater {
                    errorHandler.accept(UnsuccessfulRequestException("Command didn't send, try again!"))
                }
            }
        }
    }

    @Synchronized private fun receiveAnswer(errorHandler: Consumer<Exception>, callback: Consumer<Response>) {
        synchronized(currentChannel) {
            val timeStart = System.currentTimeMillis()

            val buffer = ByteBuffer.allocate(DEFAULT_PACKAGE_SIZE)
            var byteArray = ByteArray(0)

            var numBytesReceived = 0

            while (System.currentTimeMillis() - timeStart < SERVER_TIMEOUT_SEC * 1000) {
                try {
                    currentChannel.receive(buffer)

                    if (buffer.position() != 0) {
                        numBytesReceived += buffer.position()

                        byteArray += buffer.array()
                        buffer.clear()
                    } else if (byteArray.isNotEmpty()) {
                        logger.debug("Receive answer from server, answer size: $numBytesReceived")

                        try {
                            Platform.runLater {
                                callback.accept(ObjectSerializer().fromByteArray(byteArray))
                            }
                            return
                        } catch (e: ClassCastException) {
                            Platform.runLater {
                                errorHandler.accept(e)
                            }
                            return
                        }
                    }
                } catch (ignored: IOException) { }
            }
        }

        Platform.runLater {
            errorHandler.accept(UnsuccessfulRequestException("Server isn't available at the moment! Try again a bit later!"))
        }
    }

    override fun startUpdatesListener(): Boolean {
        try {
            val request = UpdateListenerRequest(UpdateListenerRequestType.REGISTER)

            currentChannel = datagramListenerChannel
            makeAsyncRequest(request, { logger.error("Can't initialize updates listener: ${it.message}") }) { response ->
                currentChannel = datagramChannel

                if (response.success) {
                    logger.info(response.message)

                    val listenerThread = Thread(this::updatesListener)

                    listenerThread.isDaemon = true
                    listenerThread.start()
                } else {
                    logger.error(response.message)
                }
            }
        } catch (e: UnsuccessfulRequestException) {
            logger.error("Can't initialize updates listener")
        }

        return false
    }

    private fun updatesListener() {
        val buffer = ByteBuffer.allocate(DEFAULT_PACKAGE_SIZE)
        var byteArray = ByteArray(0)

        var numBytesReceived = 0

        while (datagramListenerChannel.isConnected) {
            try {
                datagramListenerChannel.receive(buffer)

                if (buffer.position() != 0){
                    numBytesReceived += buffer.position()

                    byteArray += buffer.array()
                    buffer.clear()
                } else if (byteArray.isNotEmpty()) {
                    logger.debug("New update from server, update size: $numBytesReceived")

                    try {
                        val updateNotification: UpdateNotification = ObjectSerializer().fromByteArray(byteArray)
                        byteArray = ByteArray(0)
                        // Process notification
                        processUpdateNotification(updateNotification)
                    } catch (e: ClassCastException) {
                        logger.error("Can't deserialize update notification: ${e.message}")
                    }
                }
            } catch (ignored: IOException) { }
        }

        // On channel disconnect, close update listener
        stopUpdatesListenerProcess()

    }

    override fun stopUpdatesListenerProcess() {
        try {
            val request = UpdateListenerRequest(UpdateListenerRequestType.CLOSE)

            currentChannel = datagramListenerChannel

            makeRequest(request, {logger.error("Can't close updates listener: ${it.message}") }) {
                currentChannel = datagramChannel
            }
        } catch (e: UnsuccessfulRequestException) {
            logger.error("Can't close updates listener")
        }
    }

    private fun processUpdateNotification(updateNotification: UpdateNotification?) {
        if (updateNotification == null) {
            logger.error("Update notification deserialized incorrectly!")
            return
        }

        val route = updateNotification.entity
        val updateValue = updateNotification.updateType.value
        var success = true

        synchronized(entitiesCollection) {
            if (updateValue and UpdateType.REMOVE.value == UpdateType.REMOVE.value && success)
                success = success && entitiesCollection.removeEntityById(route)
            if (!success) print("not removed")
            if (updateValue and UpdateType.ADD.value == UpdateType.ADD.value && success)
                success = success && entitiesCollection.addNewEntity(route)
            if (!success) print("not added")
            if (updateValue and UpdateType.CLEAR.value == UpdateType.CLEAR.value && success)
                success = success && entitiesCollection.removeEntitiesByAuthor(route)
        }

        if (success)
            logger.info(
                "New update notification with route #${route.id} processed. " +
                "Update type: ${updateNotification.updateType.name}"
            )
        else
            logger.error(
                "Processing of update notification (route #${route.id}) failed!" +
                "Update type: ${updateNotification.updateType.name}"
            )
    }
}