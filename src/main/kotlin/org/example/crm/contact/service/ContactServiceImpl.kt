package org.example.crm.contact.service

import org.example.crm.contact.api.dto.ContactResponse
import org.example.crm.contact.api.dto.CreateContactRequest
import org.example.crm.contact.api.dto.UpdateContactRequest
import org.example.crm.contact.domain.CommunicationPreference
import org.example.crm.contact.domain.ContactRepository
import org.example.crm.contact.mapping.ContactMapper
import org.example.crm.customer.domain.CustomerRepository
import org.example.crm.shared.error.EntityNotFoundException
import org.example.crm.shared.error.InvalidRequestException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContactServiceImpl(
    private val contactRepository: ContactRepository,
    private val customerRepository: CustomerRepository,
    private val contactMapper: ContactMapper
) : ContactService {

    @Transactional
    override fun createContact(request: CreateContactRequest, createdBy: Long): ContactResponse {
        if (!customerRepository.existsById(request.customerId)) {
            throw EntityNotFoundException("Customer not found with id: ${request.customerId}")
        }

        val contact = contactMapper.toEntity(request).copy(createdBy = createdBy)
        return contactMapper.toResponse(contactRepository.save(contact))
    }

    @Transactional(readOnly = true)
    override fun getContact(id: Long): ContactResponse =
        contactRepository.findById(id)
            .map(contactMapper::toResponse)
            .orElseThrow { EntityNotFoundException("Contact not found with id: $id") }

    @Transactional
    override fun updateContact(id: Long, request: UpdateContactRequest): ContactResponse {
        val contact = contactRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Contact not found with id: $id") }

        val updatedContact = contactMapper.updateEntity(request, contact)
        return contactMapper.toResponse(contactRepository.save(updatedContact))
    }

    @Transactional
    override fun deleteContact(id: Long) {
        if (!contactRepository.existsById(id)) {
            throw EntityNotFoundException("Contact not found with id: $id")
        }
        contactRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun listContacts(customerId: Long, pageable: Pageable): Page<ContactResponse> {
        if (!customerRepository.existsById(customerId)) {
            throw EntityNotFoundException("Customer not found with id: $customerId")
        }
        return contactRepository.findByCustomerId(customerId, pageable).map(contactMapper::toResponse)
    }

    @Transactional(readOnly = true)
    override fun findByCustomerIdAndCommunicationPreference(
        customerId: Long,
        preference: CommunicationPreference,
        pageable: Pageable
    ): Page<ContactResponse> {
        if (!customerRepository.existsById(customerId)) {
            throw EntityNotFoundException("Customer not found with id: $customerId")
        }
        return contactRepository
            .findByCustomerIdAndCommunicationPreference(customerId, preference, pageable)
            .map(contactMapper::toResponse)
    }

    @Transactional(readOnly = true)
    override fun searchContacts(customerId: Long, query: String, pageable: Pageable): Page<ContactResponse> {
        if (!customerRepository.existsById(customerId)) {
            throw EntityNotFoundException("Customer not found with id: $customerId")
        }
        if (query.isBlank()) {
            throw InvalidRequestException("Search query cannot be blank")
        }
        return contactRepository
            .findByCustomerIdAndNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                customerId = customerId,
                name = query,
                email = query,
                phone = query,
                pageable = pageable
            )
            .map(contactMapper::toResponse)
    }
}