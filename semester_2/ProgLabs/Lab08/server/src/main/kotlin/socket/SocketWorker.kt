package socket

import entities.Route
import manager.CollectionManager
import manager.interfaces.CollectionManagerInterface
import socket.interfaces.SocketWorkerInterface
import network.Response
import network.Request
import utils.NUM_THREADS_FOR_POOL
import utils.SERVER_PORT
import utils.logger

import java.net.DatagramSocket
import java.net.SocketAddress
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool

/**
 * Class to work with UDP requests from clients (by DatagramSocket) and send responses on requests.
 * Implements SocketWorkerInterface
 *
 * @see SocketWorkerInterface
 * @see ReceiveRequest
 * @see SendResponse
 * @see Request
 * @see Response
 *
 * @author Matthew I.
 */
class SocketWorker(private val collectionManager: CollectionManagerInterface<Route>) : SocketWorkerInterface {
    private val datagramSocket: DatagramSocket = DatagramSocket(SERVER_PORT)
    private val requestsSocketAddresses = HashMap<Request, SocketAddress>()
    private val updateListenersAddresses = ArrayList<SocketAddress>()

    private val forkJoinPool = ForkJoinPool(NUM_THREADS_FOR_POOL)
    private val cachedThreadPool = Executors.newCachedThreadPool()

    init {
        logger.info("Server is listening ${datagramSocket.localSocketAddress}")
    }

    override fun getUpdates() : Request {
        return forkJoinPool.invoke(
            ReceiveRequest(datagramSocket, requestsSocketAddresses, updateListenersAddresses)
        )
    }

    override fun sendResponse(request: Request, response: Response) {
        cachedThreadPool.submit(
            SendResponse(datagramSocket, requestsSocketAddresses, request, response)
        )
        cachedThreadPool.submit(
            UpdatesDistribution(datagramSocket, updateListenersAddresses, collectionManager)
        )
    }

    override fun closeSocket() {
        if (datagramSocket.isConnected) {
            logger.info("Close server socket")

            datagramSocket.close()
        }
    }
}