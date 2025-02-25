package org.example.crm.customer.service

import org.example.crm.customer.api.dto.CreateCustomerRequest
import org.example.crm.customer.api.dto.CustomerResponse
import org.example.crm.customer.api.dto.UpdateCustomerRequest
import org.example.crm.customer.domain.Customer
import org.example.crm.customer.domain.CustomerCategory
import org.example.crm.customer.domain.CustomerRepository
import org.example.crm.customer.domain.CustomerStatus
import org.example.crm.customer.mapping.CustomerMapper
import org.example.crm.shared.error.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.Optional

class CustomerServiceTest {
    private val customerRepository: CustomerRepository = mock()
    private val customerMapper: CustomerMapper = mock()
    private val customerService = CustomerServiceImpl(customerRepository, customerMapper)

    @Test
    fun `should create customer successfully`() {
        // given
        val createdBy = 1L
        val request = CreateCustomerRequest(
            name = "Test Customer",
            company = "Test Company",
            title = "CEO",
            email = "test@example.com",
            phone = "+1234567890",
            address = "123 Test St",
            category = CustomerCategory.PREMIUM
        )

        val customer = Customer(
            id = 1L,
            name = request.name,
            company = request.company,
            title = request.title!!,
            email = request.email!!,
            phone = request.phone!!,
            address = request.address!!,
            status = CustomerStatus.ACTIVE,
            category = request.category,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            createdBy = createdBy
        )

        given(customerMapper.toEntity(request)).willReturn(customer.copy(id = null))
        given(customerRepository.save(any<Customer>())).willReturn(customer)
        given(customerMapper.toResponse(customer)).willReturn(customer.toResponse())

        // when
        val result = customerService.createCustomer(request, createdBy)

        // then
        verify(customerMapper).toEntity(request)
        verify(customerRepository).save(any<Customer>())
        assert(result.name == request.name)
        assert(result.company == request.company)
        assert(result.status == CustomerStatus.ACTIVE)
        assert(result.createdBy == createdBy)
    }

    @Test
    fun `should get customer by id successfully`() {
        // given
        val customerId = 1L
        val customer = Customer(
            id = customerId,
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
            createdBy = 1L
        )

        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer))
        given(customerMapper.toResponse(customer)).willReturn(customer.toResponse())

        // when
        val result = customerService.getCustomer(customerId)

        // then
        verify(customerRepository).findById(customerId)
        assert(result.id == customerId)
        assert(result.name == customer.name)
        assert(result.company == customer.company)
    }

    @Test
    fun `should update customer successfully`() {
        // given
        val customerId = 1L
        val request = UpdateCustomerRequest(
            email = "updated@example.com",
            phone = "+9876543210",
            status = CustomerStatus.INACTIVE
        )

        val existingCustomer = Customer(
            id = customerId,
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
            createdBy = 1L
        )

        val updatedCustomer = existingCustomer.copy(
            email = request.email!!,
            phone = request.phone!!,
            status = request.status!!,
            updatedAt = LocalDateTime.now()
        )

        given(customerRepository.findById(customerId)).willReturn(Optional.of(existingCustomer))
        given(customerMapper.updateEntity(request, existingCustomer)).willReturn(updatedCustomer)
        given(customerRepository.save(updatedCustomer)).willReturn(updatedCustomer)
        given(customerMapper.toResponse(updatedCustomer)).willReturn(updatedCustomer.toResponse())

        // when
        val result = customerService.updateCustomer(customerId, request)

        // then
        verify(customerRepository).findById(customerId)
        verify(customerRepository).save(updatedCustomer)
        assert(result.email == request.email)
        assert(result.phone == request.phone)
        assert(result.status == request.status)
    }

    @Test
    fun `should list customers with pagination`() {
        // given
        val pageable = PageRequest.of(0, 20)
        val customers = listOf(
            Customer(
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
                createdBy = 1L
            ),
            Customer(
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
                createdBy = 1L
            )
        )

        val page = PageImpl(customers, pageable, customers.size.toLong())
        given(customerRepository.findAll(pageable)).willReturn(page)
        given(customerMapper.toResponse(any())).willAnswer { invocation ->
            (invocation.arguments[0] as Customer).toResponse()
        }

        // when
        val result = customerService.listCustomers(pageable)

        // then
        verify(customerRepository).findAll(pageable)
        assert(result.content.size == 2)
        assert(result.content[0].name == "Customer 1")
        assert(result.content[1].name == "Customer 2")
    }

    @Test
    fun `should delete customer successfully`() {
        // given
        val customerId = 1L
        given(customerRepository.existsById(customerId)).willReturn(true)
        doNothing().`when`(customerRepository).deleteById(customerId)

        // when
        customerService.deleteCustomer(customerId)

        // then
        verify(customerRepository).existsById(customerId)
        verify(customerRepository).deleteById(customerId)
    }

    @Test
    fun `should throw exception when customer not found`() {
        // given
        val customerId = 999L
        given(customerRepository.findById(customerId)).willReturn(Optional.empty())

        // when/then
        assertThrows<EntityNotFoundException> {
            customerService.getCustomer(customerId)
        }

        verify(customerRepository).findById(customerId)
    }

    private fun Customer.toResponse() = CustomerResponse(
        id = id!!,
        name = name,
        company = company,
        title = title,
        email = email,
        phone = phone,
        address = address,
        status = status,
        category = category,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy
    )
}