package socket

import network.*
import network.enums.UpdateListenerRequestType
import utils.logger
import java.io.IOException
import java.lang.ClassCastException
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
    private val requestsSocketAddresses: HashMap<Request, SocketAddress>,
    private val updateListenersAddresses: ArrayList<SocketAddress>
) : RecursiveTask<Request>() {

    override fun compute(): Request {
        val packet = DatagramPacket(ByteArray(DEFAULT_PACKAGE_SIZE), DEFAULT_PACKAGE_SIZE)
        datagramSocket.receive(packet)

        val clientAddress = packet.socketAddress

        try {
            val request = ObjectSerializer().fromByteArray<Request>(packet.data)

            logger.info("Server received request from $clientAddress: $request")

            requestsSocketAddresses[request] = clientAddress

            return request
        } catch (e: ClassCastException) {
            try {
                val updateListenerRequest = ObjectSerializer().fromByteArray<UpdateListenerRequest>(packet.data)

                processNewUpdateListenerRequest(updateListenerRequest, clientAddress)
            } catch (e: ClassCastException) {
                logger.info("Incorrect request to the server from $clientAddress")
            }

            val newTask = ReceiveRequest(datagramSocket, requestsSocketAddresses, updateListenersAddresses)
            newTask.fork()

            return newTask.join()
        }
    }

    private fun processNewUpdateListenerRequest(
        updateListenerRequest: UpdateListenerRequest, clientAddress: SocketAddress
    ) {
        val requestType = updateListenerRequest.requestType

        logger.info("New update listener request from $clientAddress: ${requestType.name}")

        val success: Boolean
        val message: String

        when (requestType) {
            UpdateListenerRequestType.REGISTER -> {
                success = updateListenersAddresses.add(clientAddress)
                message = if (success) "New update listener added" else "Update listener not added!"
            }
            UpdateListenerRequestType.CLOSE -> {
                success = updateListenersAddresses.remove(clientAddress)
                message = if (success) "Update listener closed" else "Can't close update listener!"
            }
            else -> {
                success = false
                message = "Unsupported operation"
            }
        }

        // Send response to update listener

        val response = Response(success = success, message = message)
        val data = ObjectSerializer().toByteArray(response)

        try {
            val packet = DatagramPacket(data, data.size, clientAddress)

            datagramSocket.send(packet)

            logger.info("Response to $clientAddress successfully sent: $response")
        } catch (exception: IOException) {
            logger.error("Response to $clientAddress didn't send!")
        }
    }
}