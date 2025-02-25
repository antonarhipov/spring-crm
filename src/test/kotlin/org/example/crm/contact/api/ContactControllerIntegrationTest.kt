package org.example.crm.contact.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.crm.BaseIntegrationTest
import org.example.crm.contact.api.dto.ContactResponse
import org.example.crm.contact.api.dto.CreateContactRequest
import org.example.crm.contact.api.dto.UpdateContactRequest
import org.example.crm.contact.domain.CommunicationPreference
import org.example.crm.contact.service.ContactService
import org.example.crm.shared.config.SecurityConfig
import org.example.crm.shared.error.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(ContactController::class)
@Import(SecurityConfig::class)
class ContactControllerIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var contactService: ContactService

    @Test
    @WithMockUser(username = SALES_REP_USERNAME, roles = ["SALES_REPRESENTATIVE"])
    fun `should create new contact`() {
        val customerId = 1L
        val request = CreateContactRequest(
            customerId = customerId,
            name = "Test Contact",
            position = "Sales Manager",
            email = "contact@example.com",
            phone = "+1234567890",
            communicationPreference = CommunicationPreference.EMAIL
        )

        val response = ContactResponse(
            id = 1L,
            customerId = customerId,
            name = request.name,
            position = request.position,
            email = request.email,
            phone = request.phone,
            communicationPreference = request.communicationPreference,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = SALES_REP_ID
        )

        given(contactService.createContact(any(), eq(SALES_REP_ID))).willReturn(response)

        mockMvc.perform(
            post("/api/v1/customers/$customerId/contacts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value(request.name))
            .andExpect(jsonPath("$.email").value(request.email))
            .andExpect(jsonPath("$.communicationPreference").value(request.communicationPreference.name))
    }

    @Test
    @WithMockUser(username = READ_ONLY_USERNAME, roles = ["READ_ONLY"])
    fun `should get existing contact`() {
        val customerId = 1L
        val contactId = 1L
        val contact = ContactResponse(
            id = contactId,
            customerId = customerId,
            name = "Test Contact",
            position = "Sales Manager",
            email = "contact@example.com",
            phone = "+1234567890",
            communicationPreference = CommunicationPreference.EMAIL,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = SALES_REP_ID
        )

        given(contactService.getContact(contactId)).willReturn(contact)

        mockMvc.perform(get("/api/v1/customers/$customerId/contacts/$contactId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(contact.id))
            .andExpect(jsonPath("$.name").value(contact.name))
            .andExpect(jsonPath("$.email").value(contact.email))
    }

    @Test
    @WithMockUser(username = SALES_REP_USERNAME, roles = ["SALES_REPRESENTATIVE"])
    fun `should update existing contact`() {
        val customerId = 1L
        val contactId = 1L
        val request = UpdateContactRequest(
            email = "updated@example.com",
            phone = "+9876543210",
            communicationPreference = CommunicationPreference.BOTH
        )

        val response = ContactResponse(
            id = contactId,
            customerId = customerId,
            name = "Test Contact",
            position = "Sales Manager",
            email = request.email,
            phone = request.phone,
            communicationPreference = request.communicationPreference!!,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = SALES_REP_ID
        )

        given(contactService.updateContact(eq(contactId), any())).willReturn(response)

        mockMvc.perform(
            put("/api/v1/customers/$customerId/contacts/$contactId")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value(request.email))
            .andExpect(jsonPath("$.phone").value(request.phone))
            .andExpect(jsonPath("$.communicationPreference").value(request.communicationPreference?.name))
    }

    @Test
    @WithMockUser(username = SALES_REP_USERNAME, roles = ["SALES_REPRESENTATIVE"])
    fun `should delete existing contact`() {
        val customerId = 1L
        val contactId = 1L

        doNothing().`when`(contactService).deleteContact(contactId)

        mockMvc.perform(delete("/api/v1/customers/$customerId/contacts/$contactId").with(csrf()))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithMockUser(username = READ_ONLY_USERNAME, roles = ["READ_ONLY"])
    fun `should not allow read-only user to create contact`() {
        val customerId = 1L
        val request = CreateContactRequest(
            customerId = customerId,
            name = "Test Contact",
            position = "Sales Manager",
            email = "contact@example.com",
            phone = "+1234567890",
            communicationPreference = CommunicationPreference.EMAIL
        )

        mockMvc.perform(
            post("/api/v1/customers/$customerId/contacts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = SALES_REP_USERNAME, roles = ["SALES_REPRESENTATIVE"])
    fun `should list all contacts for customer`() {
        val customerId = 1L
        val contacts = listOf(
            ContactResponse(
                id = 1L,
                customerId = customerId,
                name = "Contact 1",
                position = "Sales Manager",
                email = "contact1@example.com",
                phone = "+1111111111",
                communicationPreference = CommunicationPreference.EMAIL,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                createdBy = SALES_REP_ID
            ),
            ContactResponse(
                id = 2L,
                customerId = customerId,
                name = "Contact 2",
                position = "Marketing Manager",
                email = "contact2@example.com",
                phone = "+2222222222",
                communicationPreference = CommunicationPreference.PHONE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                createdBy = SALES_REP_ID
            )
        )

        given(contactService.listContacts(eq(customerId), any())).willReturn(
            PageImpl(contacts, PageRequest.of(0, 20), contacts.size.toLong())
        )

        mockMvc.perform(get("/api/v1/customers/$customerId/contacts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].name").value("Contact 1"))
            .andExpect(jsonPath("$.content[1].name").value("Contact 2"))
    }

    @Test
    @WithMockUser(username = SALES_REP_USERNAME, roles = ["SALES_REPRESENTATIVE"])
    fun `should handle contact not found`() {
        val customerId = 1L
        val contactId = 999L

        given(contactService.getContact(contactId))
            .willThrow(EntityNotFoundException("Contact not found with id: $contactId"))

        mockMvc.perform(get("/api/v1/customers/$customerId/contacts/$contactId"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Contact not found with id: $contactId"))
    }
}