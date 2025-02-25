package org.example.crm.contact.mapping

import org.example.crm.contact.api.dto.ContactDto
import org.example.crm.contact.api.dto.ContactResponse
import org.example.crm.contact.api.dto.CreateContactRequest
import org.example.crm.contact.api.dto.UpdateContactRequest
import org.example.crm.contact.domain.Contact
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ContactMapper {
    fun toDto(contact: Contact): ContactDto = ContactDto(
        id = contact.id,
        customerId = contact.customerId,
        name = contact.name,
        position = contact.position,
        email = contact.email,
        phone = contact.phone,
        communicationPreference = contact.communicationPreference,
        createdAt = contact.createdAt,
        updatedAt = contact.updatedAt,
        createdBy = contact.createdBy
    )

    fun toResponse(contact: Contact): ContactResponse = ContactResponse(
        id = contact.id!!,
        customerId = contact.customerId,
        name = contact.name,
        position = contact.position,
        email = contact.email,
        phone = contact.phone,
        communicationPreference = contact.communicationPreference,
        createdAt = contact.createdAt,
        updatedAt = contact.updatedAt,
        createdBy = contact.createdBy
    )

    fun toEntity(request: CreateContactRequest): Contact = Contact(
        id = null,
        customerId = request.customerId,
        name = request.name,
        position = request.position ?: "",
        email = request.email ?: "",
        phone = request.phone ?: "",
        communicationPreference = request.communicationPreference,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        createdBy = 0 // Will be set by service
    )

    fun updateEntity(request: UpdateContactRequest, contact: Contact): Contact = contact.copy(
        name = request.name ?: contact.name,
        position = request.position ?: contact.position,
        email = request.email ?: contact.email,
        phone = request.phone ?: contact.phone,
        communicationPreference = request.communicationPreference ?: contact.communicationPreference,
        updatedAt = LocalDateTime.now()
    )
}
