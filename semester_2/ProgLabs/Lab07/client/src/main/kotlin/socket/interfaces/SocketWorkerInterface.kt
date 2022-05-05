package socket.interfaces

import network.Request
import network.Response

interface SocketWorkerInterface {
    fun connectToServer()
    fun closeConnection()
    fun makeRequest(request: Request): Response?
}