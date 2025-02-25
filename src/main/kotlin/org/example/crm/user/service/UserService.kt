package org.example.crm.user.service

import org.example.crm.user.api.dto.CreateUserRequest
import org.example.crm.user.api.dto.UpdateUserRequest
import org.example.crm.user.api.dto.UserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserService {
    fun createUser(request: CreateUserRequest): UserResponse
    fun getUser(id: Long): UserResponse
    fun updateUser(id: Long, request: UpdateUserRequest): UserResponse
    fun deleteUser(id: Long)
    fun listUsers(pageable: Pageable): Page<UserResponse>
    fun findByUsername(username: String): UserResponse
    fun findByEmail(email: String): UserResponse
}