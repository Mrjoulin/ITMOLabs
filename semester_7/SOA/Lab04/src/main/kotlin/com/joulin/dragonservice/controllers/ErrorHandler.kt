package com.joulin.dragonservice.controllers

import com.joulin.dragonservice.dto.ErrorResponse
import com.joulin.dragonservice.exceptions.EntityNotFoundException
import com.joulin.dragonservice.exceptions.ValidationException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.util.stream.Collectors


@RestControllerAdvice
class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException, request: HttpServletRequest
    ): ErrorResponse {
        val body: Map<String, List<String>> = HashMap()

        val error: String = ex.bindingResult
            .fieldErrors
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList())
            .joinToString("\n");

        return ErrorResponse.getError(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid arguments:\n$error"
        )
    }
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleHttpMessageNotReadableException(
        ex: HttpMessageNotReadableException, request: HttpServletRequest
    ): ErrorResponse {
        return ErrorResponse.getError(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid JSON arguments: ${ex.message}"
        )
    }


    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleAllException(e: Exception, request: HttpServletRequest): ErrorResponse {
        e.printStackTrace()
        return ErrorResponse.getError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error in ${request.method} ${request.requestURL}: ${e.message}"
        )
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(e: ValidationException): ErrorResponse {
        return ErrorResponse.getError(
            HttpStatus.BAD_REQUEST.value(),
            "Bad body: ${e.message}"
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ErrorResponse {
        return ErrorResponse.getError(
            HttpStatus.NOT_FOUND.value(),
            e.message ?: "Not found"
        )
    }
}