package org.example.crm.contact.service

import org.example.crm.contact.api.dto.ContactResponse
import org.example.crm.contact.api.dto.CreateContactRequest
import org.example.crm.contact.api.dto.UpdateContactRequest
import org.example.crm.contact.domain.CommunicationPreference
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ContactService {
    fun createContact(request: CreateContactRequest, createdBy: Long): ContactResponse
    fun getContact(id: Long): ContactResponse
    fun updateContact(id: Long, request: UpdateContactRequest): ContactResponse
    fun deleteContact(id: Long)
    fun listContacts(customerId: Long, pageable: Pageable): Page<ContactResponse>
    fun findByCustomerIdAndCommunicationPreference(
        customerId: Long,
        preference: CommunicationPreference,
        pageable: Pageable
    ): Page<ContactResponse>
    fun searchContacts(customerId: Long, query: String, pageable: Pageable): Page<ContactResponse>
}