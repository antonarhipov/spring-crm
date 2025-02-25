package org.example.crm.user.api.dto

import jakarta.validation.constraints.*
import org.example.crm.user.domain.UserRole
import org.example.crm.user.domain.UserStatus
import java.time.LocalDateTime

data class UserDto(
    val id: Long? = null,
    val username: String,
    val email: String,
    val role: UserRole,
    val status: UserStatus,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

data class CreateUserRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores and hyphens")
    val username: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    val password: String,

    @field:NotNull(message = "Role is required")
    val role: UserRole
)

data class UpdateUserRequest(
    @field:Email(message = "Invalid email format")
    val email: String? = null,

    val role: UserRole? = null,

    val status: UserStatus? = null
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val role: UserRole,
    val status: UserStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
