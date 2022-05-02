package socket

import socket.interfaces.SocketWorkerInterface
import network.DEFAULT_PACKAGE_SIZE
import network.ObjectSerializer
import network.Response
import network.Request
import utils.SERVER_PORT
import utils.logger

import java.io.IOException
import java.lang.Integer.min
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketAddress

/**
 * Class to work with UDP requests from clients (by DatagramSocket) and send responses on requests.
 * Implements SocketWorkerInterface
 *
 * @see SocketWorkerInterface
 * @see Request
 * @see Response
 * @author Matthew I.
 */
class SocketWorker : SocketWorkerInterface {
    private val datagramSocket: DatagramSocket = DatagramSocket(SERVER_PORT)
    private lateinit var currentRequestSocket: SocketAddress

    init {
        logger.info("Server is listening ${datagramSocket.localSocketAddress}")
    }

    override fun getUpdates() : Request {
        while (true) {
            try {
                val buf = ByteArray(DEFAULT_PACKAGE_SIZE)
                val packet = DatagramPacket(buf, buf.size)

                datagramSocket.receive(packet)

                val clientAddress = packet.socketAddress

                val request = ObjectSerializer.fromByteArray<Request>(packet.data)

                if (request == null) {
                    logger.info("Incorrect request to the server from $clientAddress")
                    continue
                }

                currentRequestSocket = clientAddress

                logger.info("Server received request from $clientAddress: $request")

                return request
            } catch (e: IOException) {
                logger.error("Some problem's with network!")
            }
        }
    }

    override fun sendResponse(response: Response): Boolean {
        val packetSize = DEFAULT_PACKAGE_SIZE

        val dataToSend = ObjectSerializer.toByteArray(response)

        if (dataToSend.isEmpty()) {
            logger.info("Empty data to send!")
            return false
        }

        try {
            repeat((dataToSend.size - 1) / packetSize + 1) {
                val length = min(dataToSend.size - it * packetSize, packetSize)

                val packet = DatagramPacket(
                    dataToSend, it * packetSize,
                    length, currentRequestSocket
                )

                datagramSocket.send(packet)
            }

            logger.info("Response to $currentRequestSocket successfully sent: $response")

            return true
        } catch (exception: IOException) {
            logger.error("Response to $currentRequestSocket didn't send!")
            return false
        }
    }

    override fun closeSocket() {
        if (datagramSocket.isConnected) {
            logger.info("Close server socket")

            datagramSocket.close()
        }
    }
}