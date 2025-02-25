package org.example.crm.shared.error

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.security.access.AccessDeniedException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(
        ex: EntityNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> = createErrorResponse(
        status = HttpStatus.NOT_FOUND,
        error = "Not Found",
        message = ex.message ?: "Entity not found",
        path = request.requestURI
    )

    @ExceptionHandler(InvalidRequestException::class)
    fun handleInvalidRequestException(
        ex: InvalidRequestException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> = createErrorResponse(
        status = HttpStatus.BAD_REQUEST,
        error = "Bad Request",
        message = ex.message ?: "Invalid request",
        path = request.requestURI
    )

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(
        ex: UnauthorizedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> = createErrorResponse(
        status = HttpStatus.UNAUTHORIZED,
        error = "Unauthorized",
        message = ex.message ?: "Unauthorized access",
        path = request.requestURI
    )

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> = createErrorResponse(
        status = HttpStatus.FORBIDDEN,
        error = "Forbidden",
        message = "Access denied",
        path = request.requestURI
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors
            .map { "${it.field}: ${it.defaultMessage}" }
            .joinToString(", ")
        
        return createErrorResponse(
            status = HttpStatus.BAD_REQUEST,
            error = "Validation Error",
            message = errors,
            path = request.requestURI
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> = createErrorResponse(
        status = HttpStatus.INTERNAL_SERVER_ERROR,
        error = "Internal Server Error",
        message = "An unexpected error occurred",
        path = request.requestURI
    )

    private fun createErrorResponse(
        status: HttpStatus,
        error: String,
        message: String,
        path: String
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = error,
            message = message,
            path = path
        )
        return ResponseEntity(errorResponse, status)
    }
}