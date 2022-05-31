package socket.interfaces

import network.Request
import network.Response
import java.io.Serializable

interface SocketWorkerInterface {
    fun connectToServer()
    fun closeConnection()
    fun makeRequest(request: Serializable): Response
    fun startUpdatesListener(): Boolean
    fun stopUpdatesListenerProcess()
}