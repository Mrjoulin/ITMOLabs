package socket

import network.DEFAULT_PACKAGE_SIZE
import network.SERVER_TIMEOUT_SEC
import network.ObjectSerializer
import network.Request
import network.Response
import socket.interfaces.SocketWorkerInterface
import utils.SERVER_HOST
import utils.SERVER_PORT
import utils.logger
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel


class SocketWorker: SocketWorkerInterface {
    private val socketAddress: SocketAddress = InetSocketAddress(SERVER_HOST, SERVER_PORT)
    private val datagramChannel: DatagramChannel = DatagramChannel.open()

    init {
        connectToServer()
    }

    override fun connectToServer() {
        try {
            logger.debug("Connect to server $socketAddress")

            datagramChannel.configureBlocking(false)
            datagramChannel.connect(socketAddress)
        } catch (e: IOException) {
            println("Some problems with network, can't connect to server!")
        }
    }

    override fun closeConnection() {
        if (datagramChannel.isConnected) {
            logger.debug("Disconnect from server $socketAddress")

            datagramChannel.disconnect()
        }
    }

    override fun makeRequest(request: Request): Response? {
        val dataToSend = ObjectSerializer.toByteArray(request)

        if (!datagramChannel.isConnected)
            connectToServer()

        try {
            logger.debug("Send request to the server, request size: ${dataToSend.size}")

            val buf: ByteBuffer = ByteBuffer.wrap(dataToSend)

            do {
                datagramChannel.write(buf)
            } while (buf.hasRemaining())

            val response = receiveAnswer()

            if (response == null)
                println("Server isn't available at the moment! Try again a bit later!")

            return response
        } catch (exception: IOException) {
            print("Command didn't send, try again!")
            return null
        }
    }

    private fun receiveAnswer(): Response? {
        val timeStart = System.currentTimeMillis()

        val buffer = ByteBuffer.allocate(DEFAULT_PACKAGE_SIZE)
        var byteArray = ByteArray(0)

        var numBytesReceived = 0

        while (System.currentTimeMillis() - timeStart < SERVER_TIMEOUT_SEC * 1000) {
            try {
                datagramChannel.receive(buffer)

                if (buffer.position() != 0){
                    numBytesReceived += buffer.position()

                    byteArray += buffer.array()
                    buffer.clear()
                } else if (byteArray.isNotEmpty()) {
                    logger.debug("Receive answer from server, answer size: $numBytesReceived")

                    return ObjectSerializer.fromByteArray(byteArray)
                }

            } catch (ignored: IOException) { }
        }

        return null
    }
}