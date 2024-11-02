package com.joulin.killerservice.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ErrorResponse(
    val code: Int,
    val message: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val time: LocalDateTime
) {
    companion object {
        fun getError(code: Int, message: String): ErrorResponse {
            return ErrorResponse(code, message, time = LocalDateTime.now())
        }
    }
}