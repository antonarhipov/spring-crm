package org.example.crm.user.service

import org.example.crm.shared.error.EntityNotFoundException
import org.example.crm.shared.error.InvalidRequestException
import org.example.crm.user.api.dto.CreateUserRequest
import org.example.crm.user.api.dto.UpdateUserRequest
import org.example.crm.user.api.dto.UserResponse
import org.example.crm.user.domain.UserRepository
import org.example.crm.user.mapping.UserMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    @Transactional
    override fun createUser(request: CreateUserRequest): UserResponse {
        validateNewUser(request)

        val user = userMapper.toEntity(request).copy(
            passwordHash = passwordEncoder.encode(request.password)
        )

        return userMapper.toResponse(userRepository.save(user))
    }

    @Transactional(readOnly = true)
    override fun getUser(id: Long): UserResponse =
        userRepository.findById(id)
            .map(userMapper::toResponse)
            .orElseThrow { EntityNotFoundException("User not found with id: $id") }

    @Transactional
    override fun updateUser(id: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("User not found with id: $id") }

        request.email?.let {
            if (userRepository.existsByEmail(it) && it != user.email) {
                throw InvalidRequestException("Email already exists: $it")
            }
        }

        val updatedUser = userMapper.updateEntity(request, user)
        return userMapper.toResponse(userRepository.save(updatedUser))
    }

    @Transactional
    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw EntityNotFoundException("User not found with id: $id")
        }
        userRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun listUsers(pageable: Pageable): Page<UserResponse> {
        val userPage = userRepository.findAll(pageable)
        return userPage.map(userMapper::toResponse)
    }

    @Transactional(readOnly = true)
    override fun findByUsername(username: String): UserResponse =
        userRepository.findByUsername(username)
            .map(userMapper::toResponse)
            .orElseThrow { EntityNotFoundException("User not found with username: $username") }

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): UserResponse =
        userRepository.findByEmail(email)
            .map(userMapper::toResponse)
            .orElseThrow { EntityNotFoundException("User not found with email: $email") }

    private fun validateNewUser(request: CreateUserRequest) {
        if (userRepository.existsByUsername(request.username)) {
            throw InvalidRequestException("Username already exists: ${request.username}")
        }
        if (userRepository.existsByEmail(request.email)) {
            throw InvalidRequestException("Email already exists: ${request.email}")
        }
        if (request.password.length < 8) {
            throw InvalidRequestException("Password must be at least 8 characters long")
        }
    }
}
