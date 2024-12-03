package com.joulin.dragonservice.dto

import com.joulin.dragonservice.core.Cave
import com.joulin.dragonservice.core.Coordinates
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
