package org.example.crm.contact.service

import org.example.crm.contact.api.dto.ContactResponse
import org.example.crm.contact.api.dto.CreateContactRequest
import org.example.crm.contact.api.dto.UpdateContactRequest
import org.example.crm.contact.domain.CommunicationPreference
import org.example.crm.contact.domain.Contact
import org.example.crm.contact.domain.ContactRepository
import org.example.crm.contact.mapping.ContactMapper
import org.example.crm.customer.domain.CustomerRepository
import org.example.crm.shared.error.EntityNotFoundException
import org.example.crm.shared.error.InvalidRequestException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.Optional

class ContactServiceTest {
    private val contactRepository: ContactRepository = mock()
    private val customerRepository: CustomerRepository = mock()
    private val contactMapper: ContactMapper = mock()
    private val contactService = ContactServiceImpl(contactRepository, customerRepository, contactMapper)

    @Test
    fun `should create contact successfully`() {
        // given
        val customerId = 1L
        val createdBy = 1L
        val request = CreateContactRequest(
            customerId = customerId,
            name = "Test Contact",
            position = "Sales Manager",
            email = "contact@example.com",
            phone = "+1234567890",
            communicationPreference = CommunicationPreference.EMAIL
        )

        val contact = Contact(
            id = 1L,
            customerId = customerId,
            name = request.name,
            position = request.position!!,
            email = request.email!!,
            phone = request.phone!!,
            communicationPreference = request.communicationPreference,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = createdBy
        )

        given(customerRepository.existsById(customerId)).willReturn(true)
        given(contactMapper.toEntity(request)).willReturn(contact.copy(id = null))
        given(contactRepository.save(any<Contact>())).willReturn(contact)
        given(contactMapper.toResponse(contact)).willReturn(contact.toResponse())

        // when
        val result = contactService.createContact(request, createdBy)

        // then
        verify(customerRepository).existsById(customerId)
        verify(contactMapper).toEntity(request)
        verify(contactRepository).save(any<Contact>())
        assert(result.name == request.name)
        assert(result.email == request.email)
        assert(result.communicationPreference == request.communicationPreference)
        assert(result.createdBy == createdBy)
    }

    @Test
    fun `should throw exception when creating contact for non-existent customer`() {
        // given
        val customerId = 999L
        val request = CreateContactRequest(
            customerId = customerId,
            name = "Test Contact",
            position = "Sales Manager",
            email = "contact@example.com",
            phone = "+1234567890",
            communicationPreference = CommunicationPreference.EMAIL
        )

        given(customerRepository.existsById(customerId)).willReturn(false)

        // when/then
        assertThrows<EntityNotFoundException> {
            contactService.createContact(request, 1L)
        }

        verify(customerRepository).existsById(customerId)
        verify(contactRepository, never()).save(any())
    }

    @Test
    fun `should get contact by id successfully`() {
        // given
        val contactId = 1L
        val contact = Contact(
            id = contactId,
            customerId = 1L,
            name = "Test Contact",
            position = "Sales Manager",
            email = "contact@example.com",
            phone = "+1234567890",
            communicationPreference = CommunicationPreference.EMAIL,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = 1L
        )

        given(contactRepository.findById(contactId)).willReturn(Optional.of(contact))
        given(contactMapper.toResponse(contact)).willReturn(contact.toResponse())

        // when
        val result = contactService.getContact(contactId)

        // then
        verify(contactRepository).findById(contactId)
        assert(result.id == contactId)
        assert(result.name == contact.name)
        assert(result.email == contact.email)
    }

    @Test
    fun `should update contact successfully`() {
        // given
        val contactId = 1L
        val request = UpdateContactRequest(
            email = "updated@example.com",
            phone = "+9876543210",
            communicationPreference = CommunicationPreference.BOTH
        )

        val existingContact = Contact(
            id = contactId,
            customerId = 1L,
            name = "Test Contact",
            position = "Sales Manager",
            email = "contact@example.com",
            phone = "+1234567890",
            communicationPreference = CommunicationPreference.EMAIL,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = 1L
        )

        val updatedContact = existingContact.copy(
            email = request.email!!,
            phone = request.phone!!,
            communicationPreference = request.communicationPreference!!,
            updatedAt = LocalDateTime.now()
        )

        given(contactRepository.findById(contactId)).willReturn(Optional.of(existingContact))
        given(contactMapper.updateEntity(request, existingContact)).willReturn(updatedContact)
        given(contactRepository.save(updatedContact)).willReturn(updatedContact)
        given(contactMapper.toResponse(updatedContact)).willReturn(updatedContact.toResponse())

        // when
        val result = contactService.updateContact(contactId, request)

        // then
        verify(contactRepository).findById(contactId)
        verify(contactRepository).save(updatedContact)
        assert(result.email == request.email)
        assert(result.phone == request.phone)
        assert(result.communicationPreference == request.communicationPreference)
    }

    @Test
    fun `should list contacts for customer with pagination`() {
        // given
        val customerId = 1L
        val pageable = PageRequest.of(0, 20)
        val contacts = listOf(
            Contact(
                id = 1L,
                customerId = customerId,
                name = "Contact 1",
                position = "Sales Manager",
                email = "contact1@example.com",
                phone = "+1111111111",
                communicationPreference = CommunicationPreference.EMAIL,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                createdBy = 1L
            ),
            Contact(
                id = 2L,
                customerId = customerId,
                name = "Contact 2",
                position = "Marketing Manager",
                email = "contact2@example.com",
                phone = "+2222222222",
                communicationPreference = CommunicationPreference.PHONE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                createdBy = 1L
            )
        )

        given(customerRepository.existsById(customerId)).willReturn(true)
        val page = PageImpl(contacts, pageable, contacts.size.toLong())
        given(contactRepository.findByCustomerId(customerId, pageable)).willReturn(page)
        given(contactMapper.toResponse(any())).willAnswer { invocation ->
            (invocation.arguments[0] as Contact).toResponse()
        }

        // when
        val result = contactService.listContacts(customerId, pageable)

        // then
        verify(customerRepository).existsById(customerId)
        verify(contactRepository).findByCustomerId(customerId, pageable)
        assert(result.content.size == 2)
        assert(result.content[0].name == "Contact 1")
        assert(result.content[1].name == "Contact 2")
    }

    @Test
    fun `should delete contact successfully`() {
        // given
        val contactId = 1L
        given(contactRepository.existsById(contactId)).willReturn(true)
        doNothing().`when`(contactRepository).deleteById(contactId)

        // when
        contactService.deleteContact(contactId)

        // then
        verify(contactRepository).existsById(contactId)
        verify(contactRepository).deleteById(contactId)
    }

    private fun Contact.toResponse() = ContactResponse(
        id = id!!,
        customerId = customerId,
        name = name,
        position = position,
        email = email,
        phone = phone,
        communicationPreference = communicationPreference,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy
    )
}