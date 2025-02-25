package org.example.crm.customer.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("customers")
data class Customer(
    @Id val id: Long? = null,
    val name: String,
    val company: String,
    val title: String,
    val email: String,
    val phone: String,
    val address: String,
    val status: CustomerStatus,
    val category: CustomerCategory,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val createdBy: Long
)

enum class CustomerStatus {
    ACTIVE, INACTIVE
}

enum class CustomerCategory {
    REGULAR, PREMIUM, ENTERPRISE
}