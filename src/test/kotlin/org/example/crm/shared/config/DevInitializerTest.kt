package org.example.crm.shared.config

import org.example.crm.user.domain.User
import org.example.crm.user.domain.UserRepository
import org.example.crm.user.domain.UserRole
import org.example.crm.user.domain.UserStatus
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.DefaultApplicationArguments
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.Optional

class DevInitializerTest {

    @Test
    fun `should update admin password and print it to console`() {
        // given
        val userRepository = mock(UserRepository::class.java)
        val passwordEncoder = mock(PasswordEncoder::class.java)
        
        val admin = User(
            id = 1L,
            username = "admin",
            email = "admin@example.com",
            passwordHash = "old_hash",
            role = UserRole.ADMIN,
            status = UserStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        `when`(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin))
        `when`(passwordEncoder.encode(any())).thenReturn("new_hash")
        
        val devInitializer = DevInitializer(userRepository, passwordEncoder)

        // when
        devInitializer.run(DefaultApplicationArguments())

        // then
        verify(userRepository).findByUsername("admin")
        verify(passwordEncoder).encode(any())
        verify(userRepository).save(argThat { user ->
            user.passwordHash == "new_hash" && user.id == 1L
        })
    }
}