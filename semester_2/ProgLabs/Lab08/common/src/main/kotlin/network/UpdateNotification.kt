package network

import entities.Route
import network.enums.UpdateType
import java.io.Serializable

data class UpdateNotification (
    var updateType: UpdateType,
    val entity: Route
) : Serializable