package com.joulin.dragonservice.dto

import com.joulin.dragonservice.core.Cave
import com.joulin.dragonservice.core.Coordinates
import java.time.LocalDateTime

data class CaveRequest(
    val coordinates: Coordinates
) {
    fun validateCave(): Cave {
        return Cave(
            coordinates = coordinates,
            creationDate = LocalDateTime.now()
        )
    }
}
