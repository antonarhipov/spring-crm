package org.example.crm.shared.config

import org.example.crm.user.domain.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Profile("dev")
class DevInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        val generatedPassword = UUID.randomUUID().toString().take(8)
        println("\n=== Development Mode ===")
        println("Generated admin password: $generatedPassword")
        println("======================\n")

        val admin = userRepository.findByUsername("admin")
            .orElseThrow { IllegalStateException("Admin user not found") }

        val updatedAdmin = admin.copy(
            passwordHash = passwordEncoder.encode(generatedPassword),
            updatedAt = java.time.LocalDateTime.now()
        )
        userRepository.save(updatedAdmin)
    }
}
