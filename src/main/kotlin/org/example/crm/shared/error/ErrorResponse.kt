package org.example.crm.shared.error

import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)

class EntityNotFoundException(message: String) : RuntimeException(message)
class InvalidRequestException(message: String) : RuntimeException(message)
class UnauthorizedException(message: String) : RuntimeException(message)