package client

import entities.Route
import network.TOKEN_ENV_NAME
import socket.interfaces.SocketWorkerInterface
import socket.SocketWorker
import utils.LANGUAGES_BASE_NAME_PREFIX
import java.io.InputStreamReader
import java.io.PrintStream
import java.util.*

data class ClientSession(
    var userToken: String = System.getenv(TOKEN_ENV_NAME) ?: "",
    var username: String = "",
    var currentInput: InputStreamReader = InputStreamReader(System.`in`),
    var currentOutput: PrintStream = System.out,
    var collectionManager: CollectionManager = CollectionManager(),
    var socketWorker: SocketWorkerInterface = SocketWorker(collectionManager),
    var languageChanged: Boolean = false,
    var currentLanguage: ResourceBundle = ResourceBundle.getBundle(LANGUAGES_BASE_NAME_PREFIX)
)
