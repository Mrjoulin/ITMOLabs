package socket

import client.CollectionManager
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
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import kotlin.jvm.Throws


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

    @Throws(UnsuccessfulRequestException::class)
    @Synchronized override fun makeRequest(request: Serializable): Response {
        val dataToSend = ObjectSerializer().toByteArray(request)

        if (!currentChannel.isConnected)
            connectToServer()

        try {
            logger.debug("Send request to the server, request size: ${dataToSend.size}")

            val buf: ByteBuffer = ByteBuffer.wrap(dataToSend)

            do {
                currentChannel.write(buf)
            } while (buf.hasRemaining())

            return receiveAnswer()
                ?: throw UnsuccessfulRequestException("Server isn't available at the moment! Try again a bit later!")
        } catch (exception: IOException) {
            throw UnsuccessfulRequestException("Command didn't send, try again!")
        }
    }

    private fun receiveAnswer(): Response? {
        val timeStart = System.currentTimeMillis()

        val buffer = ByteBuffer.allocate(DEFAULT_PACKAGE_SIZE)
        var byteArray = ByteArray(0)

        var numBytesReceived = 0

        while (System.currentTimeMillis() - timeStart < SERVER_TIMEOUT_SEC * 1000) {
            try {
                currentChannel.receive(buffer)

                if (buffer.position() != 0){
                    numBytesReceived += buffer.position()

                    byteArray += buffer.array()
                    buffer.clear()
                } else if (byteArray.isNotEmpty()) {
                    logger.debug("Receive answer from server, answer size: $numBytesReceived")

                    return ObjectSerializer().fromByteArray(byteArray)
                }

            } catch (ignored: IOException) { }
        }

        return null
    }

    override fun startUpdatesListener(): Boolean {
        try {
            val request = UpdateListenerRequest(UpdateListenerRequestType.REGISTER)

            currentChannel = datagramListenerChannel
            val response = makeRequest(request)
            currentChannel = datagramChannel

            if (response.success) {
                logger.info(response.message)

                val listenerThread = Thread(this::updatesListener)
                listenerThread.isDaemon = true

                listenerThread.start()

                return true
            } else {
                logger.error(response.message)
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

                    val updateNotification: UpdateNotification? = ObjectSerializer().fromByteArray(byteArray)
                    byteArray = ByteArray(0)
                    // Process notification
                    processUpdateNotification(updateNotification)
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

            makeRequest(request)

            currentChannel = datagramChannel
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