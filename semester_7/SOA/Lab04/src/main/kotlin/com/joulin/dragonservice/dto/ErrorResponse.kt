package com.joulin.dragonservice.dto

import com.fasterxml.jackson.annotation.JsonFormat
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Getter
@NoArgsConstructor
@AllArgsConstructor
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