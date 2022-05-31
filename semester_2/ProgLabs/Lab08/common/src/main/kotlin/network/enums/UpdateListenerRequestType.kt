package network.enums

import java.io.Serializable

enum class UpdateListenerRequestType : Serializable {
    REGISTER(),
    CLOSE()
}