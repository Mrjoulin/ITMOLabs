package socket.interfaces

import network.Request
import network.Response

/**
 * Interface of SocketWorker class.
 * Describes the typical behavior of a class for handling requests and sending responses
 *
 * @author Matthew I.
 */
interface SocketWorkerInterface {
    fun getUpdates(): Request
    fun sendResponse(request: Request, response: Response)
    fun closeSocket()
}