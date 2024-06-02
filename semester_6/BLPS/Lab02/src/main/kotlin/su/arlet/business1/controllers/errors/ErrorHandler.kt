package su.arlet.business1.controllers.errors

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import su.arlet.business1.exceptions.*

@RestControllerAdvice
class ErrorHandler {
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleAllException(e: Exception, request: HttpServletRequest) {
        println("Error in ${request.method} ${request.requestURL}: ${e.message}")
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(e: ValidationException): String {
        return "Bad body: ${e.message}"
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFoundException(e: EntityNotFoundException): String {
        return "Not found"
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleUserNotFoundException(e: UserNotFoundException): String {
        return "user not found"
    }

    @ExceptionHandler(UnsupportedStatusChangeException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleUnsupportedStatusChangeException(e: UnsupportedStatusChangeException): String {
        return "unsupported status change"
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleUserAlreadyExistsException(e: UserAlreadyExistsException): String {
        return "this username has been already taken"
    }

    @ExceptionHandler(UnauthorizedError::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleUnauthorizedError(e: UnauthorizedError): String {
        return "unauthorized"
    }

    @ExceptionHandler(PermissionDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handlePermissionDeniedException(e: PermissionDeniedException): String {
        return "you don't have access to this ${e.message}"
    }

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDeniedException(e: PermissionDeniedException): String {
        return "you don't have access to this ${e.message}"
    }
}