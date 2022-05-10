package server

import socket.interfaces.SocketWorkerInterface
import invoker.interfaces.InvokerInterface
import network.Request
import network.Response
import utils.logger

/**
 * Class to process new request from client
 * Implements Runnable interface
 *
 * @param invoker Invoker object to invoke received request
 * @param socketWorker SocketWorker object to send response on given request, if null just print response message to console
 * @param request Request object to process
 *
 * @see InvokerInterface
 * @see SocketWorkerInterface
 * @see Request
 *
 * @author Matthew I.
 */
class ProcessRequest (
    private val invoker: InvokerInterface,
    private val socketWorker: SocketWorkerInterface?,
    private val request: Request
) : Runnable {

    override fun run() {
        // Invoke request to command
        val response = try {
            invoker.execute(request)
        } catch (e: IllegalStateException) {
            Response(success = false, message = e.message ?: "Illegal State")
        } catch (e: Exception) {
            logger.error("Got exception while processing new request", e)
            Response(success = false, message = "Got error on server while processing command! Try again later")
        }

        // Send response if socketWorker given, else just print message to console
        if (socketWorker != null)
            socketWorker.sendResponse(request, response)
        else
            println(response.message)
    }
}