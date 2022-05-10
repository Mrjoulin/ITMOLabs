package socket

import network.DEFAULT_PACKAGE_SIZE
import network.ObjectSerializer
import network.Request
import utils.logger
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketAddress
import java.util.concurrent.RecursiveTask

/**
 * Class to receive new request from client.
 * Extends RecursiveTask<Request>
 *
 * @param datagramSocket DatagramSocket object to receive new request
 * @param requestsSocketAddresses HashMap<Request, SocketAddress> to store client socket address by his request
 *
 * @see Request
 *
 * @author Matthew I.
 */
class ReceiveRequest (
    private val datagramSocket: DatagramSocket,
    private val requestsSocketAddresses: HashMap<Request, SocketAddress>
) : RecursiveTask<Request>() {

    override fun compute(): Request {
        val packet = DatagramPacket(ByteArray(DEFAULT_PACKAGE_SIZE), DEFAULT_PACKAGE_SIZE)
        datagramSocket.receive(packet)

        val clientAddress = packet.socketAddress

        val request = ObjectSerializer.fromByteArray<Request>(packet.data)

        if (request == null) {
            logger.info("Incorrect request to the server from $clientAddress")

            val newTask = ReceiveRequest(datagramSocket, requestsSocketAddresses)
            newTask.fork()

            return newTask.join()
        }

        logger.info("Server received request from $clientAddress: $request")

        requestsSocketAddresses[request] = clientAddress

        return request
    }
}