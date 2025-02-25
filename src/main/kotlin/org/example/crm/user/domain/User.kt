package org.example.crm.user.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("users")
data class User(
    @Id val id: Long? = null,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val status: UserStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class UserRole {
    ADMIN, SALES_MANAGER, SALES_REPRESENTATIVE, READ_ONLY
}

enum class UserStatus {
    ACTIVE, INACTIVE
}