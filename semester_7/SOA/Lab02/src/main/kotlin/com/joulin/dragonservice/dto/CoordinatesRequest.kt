package com.joulin.dragonservice.dto

import com.joulin.dragonservice.core.Coordinates
import com.joulin.dragonservice.exceptions.ValidationException

data class CoordinatesRequest(
    val x: Double?,
    val y: Double?
) {
    fun validateCoordinates(): Coordinates {
        val xValue = validateX() ?: throw ValidationException("coordinates x should be correct number greater then -438")
        val yValue = validateY() ?: throw ValidationException("coordinates y should be not null")
        return Coordinates(xValue, yValue)
    }

    fun validateX(): Double? {
        return if (x != null && x > -438) x else null
    }
    fun validateY(): Double? {
        return y
    }
}
