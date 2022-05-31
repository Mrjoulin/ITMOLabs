package network.enums

import java.io.Serializable

enum class UpdateType(val value: Int) : Serializable {
    ADD(1),
    REMOVE(2),
    UPDATE(3),
    CLEAR(4)
}