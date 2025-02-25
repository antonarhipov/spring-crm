package org.example.crm.user.mapping

import org.example.crm.user.api.dto.CreateUserRequest
import org.example.crm.user.api.dto.UpdateUserRequest
import org.example.crm.user.api.dto.UserDto
import org.example.crm.user.api.dto.UserResponse
import org.example.crm.user.domain.User
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface UserMapper {
    fun toDto(user: User): UserDto
    
    fun toResponse(user: User): UserResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    fun toEntity(request: CreateUserRequest): User

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    fun updateEntity(request: UpdateUserRequest, @MappingTarget user: User): User
}