package org.example.crm.contact.api.dto

import jakarta.validation.constraints.*
import org.example.crm.contact.domain.CommunicationPreference
import java.time.LocalDateTime

data class ContactDto(
    val id: Long? = null,
    val customerId: Long,
    val name: String,
    val position: String,
    val email: String,
    val phone: String,
    val communicationPreference: CommunicationPreference,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: Long
)

data class CreateContactRequest(
    @field:NotNull(message = "Customer ID is required")
    val customerId: Long,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    @field:Size(max = 100, message = "Position cannot exceed 100 characters")
    val position: String? = null,

    @field:Email(message = "Invalid email format")
    val email: String? = null,

    @field:Pattern(regexp = "^\\+?[1-9]\\d{1,14}\$", message = "Invalid phone number format")
    val phone: String? = null,

    @field:NotNull(message = "Communication preference is required")
    val communicationPreference: CommunicationPreference
)

data class UpdateContactRequest(
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String? = null,

    @field:Size(max = 100, message = "Position cannot exceed 100 characters")
    val position: String? = null,

    @field:Email(message = "Invalid email format")
    val email: String? = null,

    @field:Pattern(regexp = "^\\+?[1-9]\\d{1,14}\$", message = "Invalid phone number format")
    val phone: String? = null,

    val communicationPreference: CommunicationPreference? = null
)

data class ContactResponse(
    val id: Long,
    val customerId: Long,
    val name: String,
    val position: String?,
    val email: String?,
    val phone: String?,
    val communicationPreference: CommunicationPreference,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: Long
)