package socket

import network.DEFAULT_PACKAGE_SIZE
import network.ObjectSerializer
import network.Request
import network.Response
import utils.logger
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketAddress

/**
 * Class to send response to client for given request.
 * Implements Runnable interface
 *
 * @param datagramSocket DatagramSocket class to send response
 * @param requestsSocketAddresses Map with requests keys and clients socket address values
 * @param request Request to be answered
 * @param response Response for this request
 *
 * @see Request
 * @see Response
 *
 * @author Matthew I.
 */
class SendResponse(
    private val datagramSocket: DatagramSocket,
    private val requestsSocketAddresses: HashMap<Request, SocketAddress>,
    private val request: Request,
    private val response: Response
): Runnable {
    override fun run() {
        if (!requestsSocketAddresses.containsKey(request)) {
            logger.warn("Can't find address witch from request $request comes from")
            return
        }

        logger.debug("Sending response to ${requestsSocketAddresses[request]}: $response")

        val packetSize = DEFAULT_PACKAGE_SIZE
        val dataToSend = try {
            ObjectSerializer().toByteArray(response)
        } catch (e: Exception) {
            logger.error("Get exception while serializing response: $response", e)
            return
        }

        if (dataToSend.isEmpty()) {
            logger.info("Empty data to send!")
            return
        }

        try {
            logger.debug("Start sending data to ${requestsSocketAddresses[request]}, len data: ${dataToSend.size}")

            val numPackets = (dataToSend.size - 1) / packetSize + 1

            repeat(numPackets) {
                val length = Integer.min(dataToSend.size - it * packetSize, packetSize)

                val packet = DatagramPacket(
                    dataToSend, it * packetSize,
                    length, requestsSocketAddresses[request]
                )

                datagramSocket.send(packet)
                logger.debug("Sent packet ${it + 1}/$numPackets")
            }

            logger.info("Response to ${requestsSocketAddresses[request]} successfully sent: $response")
        } catch (exception: IOException) {
            logger.error("Response to ${requestsSocketAddresses[request]} didn't send!")
        }
    }
}