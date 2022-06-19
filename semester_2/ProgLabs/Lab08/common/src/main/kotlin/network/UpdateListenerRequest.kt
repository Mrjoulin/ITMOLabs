package network

import network.enums.UpdateListenerRequestType
import java.io.Serializable

data class UpdateListenerRequest(
    val requestType: UpdateListenerRequestType
) : Serializable
