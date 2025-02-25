package org.example.crm.contact.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("contacts")
data class Contact(
    @Id val id: Long? = null,
    val customerId: Long,
    val name: String,
    val position: String,
    val email: String,
    val phone: String,
    val communicationPreference: CommunicationPreference,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val createdBy: Long
)

enum class CommunicationPreference {
    EMAIL, PHONE, BOTH
}