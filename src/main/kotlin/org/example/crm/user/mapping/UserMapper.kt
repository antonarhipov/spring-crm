package org.example.crm.user.mapping

import org.example.crm.user.api.dto.CreateUserRequest
import org.example.crm.user.api.dto.UpdateUserRequest
import org.example.crm.user.api.dto.UserDto
import org.example.crm.user.api.dto.UserResponse
import org.example.crm.user.domain.User
import org.example.crm.user.domain.UserStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserMapper {
    fun toDto(user: User): UserDto = UserDto(
        id = user.id,
        username = user.username,
        email = user.email,
        role = user.role,
        status = user.status,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt
    )

    fun toResponse(user: User): UserResponse = UserResponse(
        id = user.id!!,
        username = user.username,
        email = user.email,
        role = user.role,
        status = user.status,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt
    )

    fun toEntity(request: CreateUserRequest): User = User(
        id = null,
        username = request.username,
        email = request.email,
        passwordHash = "", // Will be set by service
        role = request.role,
        status = UserStatus.ACTIVE,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    fun updateEntity(request: UpdateUserRequest, user: User): User = user.copy(
        email = request.email ?: user.email,
        role = request.role ?: user.role,
        status = request.status ?: user.status,
        updatedAt = LocalDateTime.now()
    )
}
