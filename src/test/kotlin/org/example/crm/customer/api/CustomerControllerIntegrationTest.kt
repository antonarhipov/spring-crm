package org.example.crm.customer.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.crm.BaseIntegrationTest
import org.example.crm.customer.api.dto.CreateCustomerRequest
import org.example.crm.customer.api.dto.CustomerResponse
import org.example.crm.customer.api.dto.UpdateCustomerRequest
import org.example.crm.customer.domain.CustomerCategory
import org.example.crm.customer.domain.CustomerStatus
import org.example.crm.customer.service.CustomerService
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

@WebMvcTest(CustomerController::class)
@Import(SecurityConfig::class)
class CustomerControllerIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var customerService: CustomerService

    @Test
    @WithMockUser(username = MANAGER_USERNAME, roles = ["SALES_MANAGER"])
    fun `should create new customer`() {
        val request = CreateCustomerRequest(
            name = "Test Customer",
            company = "Test Company",
            title = "CEO",
            email = "test@example.com",
            phone = "+1234567890",
            address = "123 Test St",
            category = CustomerCategory.PREMIUM
        )

        val response = CustomerResponse(
            id = 1L,
            name = request.name,
            company = request.company,
            title = request.title,
            email = request.email,
            phone = request.phone,
            address = request.address,
            status = CustomerStatus.ACTIVE,
            category = request.category,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = MANAGER_ID
        )

        given(customerService.createCustomer(any(), eq(MANAGER_ID))).willReturn(response)

        mockMvc.perform(
            post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value(request.name))
            .andExpect(jsonPath("$.company").value(request.company))
            .andExpect(jsonPath("$.status").value(CustomerStatus.ACTIVE.name))
            .andExpect(jsonPath("$.category").value(request.category.name))
    }

    @Test
    @WithMockUser(username = SALES_REP_USERNAME, roles = ["SALES_REPRESENTATIVE"])
    fun `should get existing customer`() {
        val customer = CustomerResponse(
            id = 1L,
            name = "Test Customer",
            company = "Test Company",
            title = "CEO",
            email = "test@example.com",
            phone = "+1234567890",
            address = "123 Test St",
            status = CustomerStatus.ACTIVE,
            category = CustomerCategory.PREMIUM,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = MANAGER_ID
        )

        given(customerService.getCustomer(1L)).willReturn(customer)

        mockMvc.perform(get("/api/v1/customers/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.company").value(customer.company))
    }

    @Test
    @WithMockUser(username = MANAGER_USERNAME, roles = ["SALES_MANAGER"])
    fun `should update existing customer`() {
        val request = UpdateCustomerRequest(
            email = "updated@example.com",
            phone = "+9876543210",
            status = CustomerStatus.INACTIVE
        )

        val response = CustomerResponse(
            id = 1L,
            name = "Test Customer",
            company = "Test Company",
            title = "CEO",
            email = request.email,
            phone = request.phone,
            address = "123 Test St",
            status = request.status!!,
            category = CustomerCategory.PREMIUM,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = MANAGER_ID
        )

        given(customerService.updateCustomer(eq(1L), any())).willReturn(response)

        mockMvc.perform(
            put("/api/v1/customers/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value(request.email))
            .andExpect(jsonPath("$.phone").value(request.phone))
            .andExpect(jsonPath("$.status").value(request.status?.name))
    }

    @Test
    @WithMockUser(username = MANAGER_USERNAME, roles = ["SALES_MANAGER"])
    fun `should delete existing customer`() {
        doNothing().`when`(customerService).deleteCustomer(1L)

        mockMvc.perform(delete("/api/v1/customers/1").with(csrf()))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithMockUser(username = READ_ONLY_USERNAME, roles = ["READ_ONLY"])
    fun `should not allow read-only user to create customer`() {
        val request = CreateCustomerRequest(
            name = "Test Customer",
            company = "Test Company",
            title = "CEO",
            email = "test@example.com",
            phone = "+1234567890",
            address = "123 Test St",
            category = CustomerCategory.PREMIUM
        )

        mockMvc.perform(
            post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = SALES_REP_USERNAME, roles = ["SALES_REPRESENTATIVE"])
    fun `should list all customers`() {
        val customers = listOf(
            CustomerResponse(
                id = 1L,
                name = "Customer 1",
                company = "Company 1",
                title = "CEO",
                email = "customer1@example.com",
                phone = "+1111111111",
                address = "Address 1",
                status = CustomerStatus.ACTIVE,
                category = CustomerCategory.PREMIUM,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                createdBy = MANAGER_ID
            ),
            CustomerResponse(
                id = 2L,
                name = "Customer 2",
                company = "Company 2",
                title = "CTO",
                email = "customer2@example.com",
                phone = "+2222222222",
                address = "Address 2",
                status = CustomerStatus.ACTIVE,
                category = CustomerCategory.ENTERPRISE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                createdBy = SALES_REP_ID
            )
        )

        given(customerService.listCustomers(any())).willReturn(
            PageImpl(customers, PageRequest.of(0, 20), customers.size.toLong())
        )

        mockMvc.perform(get("/api/v1/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].name").value("Customer 1"))
            .andExpect(jsonPath("$.content[1].name").value("Customer 2"))
    }

    @Test
    @WithMockUser(username = SALES_REP_USERNAME, roles = ["SALES_REPRESENTATIVE"])
    fun `should handle customer not found`() {
        given(customerService.getCustomer(999L)).willThrow(EntityNotFoundException("Customer not found with id: 999"))

        mockMvc.perform(get("/api/v1/customers/999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Customer not found with id: 999"))
    }
}