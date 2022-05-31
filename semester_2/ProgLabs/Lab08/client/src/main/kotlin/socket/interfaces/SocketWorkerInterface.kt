package socket.interfaces

import javafx.util.Callback
import network.Request
import network.Response
import java.io.Serializable
import java.lang.Exception
import java.util.function.Consumer

interface SocketWorkerInterface {
    fun connectToServer()
    fun closeConnection()
    fun makeAsyncRequest(request: Serializable, errorHandler: Consumer<Exception>, callback: Consumer<Response>)
    fun startUpdatesListener(): Boolean
    fun stopUpdatesListenerProcess()
}