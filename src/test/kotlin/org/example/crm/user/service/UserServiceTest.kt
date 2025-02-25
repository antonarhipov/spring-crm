package org.example.crm.user.service

import org.example.crm.shared.error.EntityNotFoundException
import org.example.crm.shared.error.InvalidRequestException
import org.example.crm.user.api.dto.CreateUserRequest
import org.example.crm.user.api.dto.UpdateUserRequest
import org.example.crm.user.domain.User
import org.example.crm.user.domain.UserRepository
import org.example.crm.user.domain.UserRole
import org.example.crm.user.domain.UserStatus
import org.example.crm.user.mapping.UserMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.Optional

class UserServiceTest {
    private val userRepository: UserRepository = mock()
    private val userMapper: UserMapper = mock()
    private val passwordEncoder: PasswordEncoder = mock()
    private val userService = UserServiceImpl(userRepository, userMapper, passwordEncoder)

    @Test
    fun `should create user successfully`() {
        // given
        val request = CreateUserRequest(
            username = "testuser",
            email = "test@example.com",
            password = "password123",
            role = UserRole.SALES_REPRESENTATIVE
        )

        val hashedPassword = "hashed_password"
        val user = User(
            id = 1L,
            username = request.username,
            email = request.email,
            passwordHash = hashedPassword,
            role = request.role,
            status = UserStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        given(userRepository.existsByUsername(request.username)).willReturn(false)
        given(userRepository.existsByEmail(request.email)).willReturn(false)
        given(passwordEncoder.encode(request.password)).willReturn(hashedPassword)
        given(userMapper.toEntity(request)).willReturn(user.copy(id = null, passwordHash = ""))
        given(userRepository.save(any<User>())).willReturn(user)
        given(userMapper.toResponse(user)).willReturn(user.toResponse())

        // when
        val result = userService.createUser(request)

        // then
        verify(userRepository).existsByUsername(request.username)
        verify(userRepository).existsByEmail(request.email)
        verify(passwordEncoder).encode(request.password)
        verify(userRepository).save(any<User>())
        assert(result.username == request.username)
        assert(result.email == request.email)
        assert(result.role == request.role)
        assert(result.status == UserStatus.ACTIVE)
    }

    @Test
    fun `should throw exception when username already exists`() {
        // given
        val request = CreateUserRequest(
            username = "existing",
            email = "test@example.com",
            password = "password123",
            role = UserRole.SALES_REPRESENTATIVE
        )

        given(userRepository.existsByUsername(request.username)).willReturn(true)

        // when/then
        assertThrows<InvalidRequestException> {
            userService.createUser(request)
        }

        verify(userRepository).existsByUsername(request.username)
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `should update user successfully`() {
        // given
        val userId = 1L
        val request = UpdateUserRequest(
            email = "updated@example.com",
            status = UserStatus.INACTIVE
        )

        val existingUser = User(
            id = userId,
            username = "testuser",
            email = "test@example.com",
            passwordHash = "hashed_password",
            role = UserRole.SALES_REPRESENTATIVE,
            status = UserStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val updatedUser = existingUser.copy(
            email = request.email!!,
            status = request.status!!,
            updatedAt = LocalDateTime.now()
        )

        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser))
        given(userRepository.existsByEmail(request.email!!)).willReturn(false)
        given(userMapper.updateEntity(request, existingUser)).willReturn(updatedUser)
        given(userRepository.save(updatedUser)).willReturn(updatedUser)
        given(userMapper.toResponse(updatedUser)).willReturn(updatedUser.toResponse())

        // when
        val result = userService.updateUser(userId, request)

        // then
        verify(userRepository).findById(userId)
        verify(userRepository).existsByEmail(request.email!!)
        verify(userRepository).save(updatedUser)
        assert(result.email == request.email)
        assert(result.status == request.status)
    }

    @Test
    fun `should throw exception when updating non-existent user`() {
        // given
        val userId = 999L
        val request = UpdateUserRequest(email = "new@example.com")

        given(userRepository.findById(userId)).willReturn(Optional.empty())

        // when/then
        assertThrows<EntityNotFoundException> {
            userService.updateUser(userId, request)
        }

        verify(userRepository).findById(userId)
        verify(userRepository, never()).save(any())
    }

    private fun User.toResponse() = org.example.crm.user.api.dto.UserResponse(
        id = id!!,
        username = username,
        email = email,
        role = role,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    @Test
    fun `should get user by id successfully`() {
        // given
        val userId = 1L
        val user = User(
            id = userId,
            username = "testuser",
            email = "test@example.com",
            passwordHash = "hashed_password",
            role = UserRole.SALES_REPRESENTATIVE,
            status = UserStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        given(userRepository.findById(userId)).willReturn(Optional.of(user))
        given(userMapper.toResponse(user)).willReturn(user.toResponse())

        // when
        val result = userService.getUser(userId)

        // then
        verify(userRepository).findById(userId)
        assert(result.id == userId)
        assert(result.username == user.username)
        assert(result.email == user.email)
    }

    @Test
    fun `should delete user successfully`() {
        // given
        val userId = 1L
        given(userRepository.existsById(userId)).willReturn(true)
        doNothing().`when`(userRepository).deleteById(userId)

        // when
        userService.deleteUser(userId)

        // then
        verify(userRepository).existsById(userId)
        verify(userRepository).deleteById(userId)
    }

    @Test
    fun `should throw exception when deleting non-existent user`() {
        // given
        val userId = 999L
        given(userRepository.existsById(userId)).willReturn(false)

        // when/then
        assertThrows<EntityNotFoundException> {
            userService.deleteUser(userId)
        }

        verify(userRepository).existsById(userId)
        verify(userRepository, never()).deleteById(any())
    }
}
