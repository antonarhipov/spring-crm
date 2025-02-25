package org.example.crm.customer.mapping

import org.example.crm.customer.api.dto.CreateCustomerRequest
import org.example.crm.customer.api.dto.CustomerDto
import org.example.crm.customer.api.dto.CustomerResponse
import org.example.crm.customer.api.dto.UpdateCustomerRequest
import org.example.crm.customer.domain.Customer
import org.example.crm.customer.domain.CustomerStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CustomerMapper {
    fun toDto(customer: Customer): CustomerDto = CustomerDto(
        id = customer.id,
        name = customer.name,
        company = customer.company,
        title = customer.title,
        email = customer.email,
        phone = customer.phone,
        address = customer.address,
        status = customer.status,
        category = customer.category,
        createdAt = customer.createdAt,
        updatedAt = customer.updatedAt,
        createdBy = customer.createdBy
    )

    fun toResponse(customer: Customer): CustomerResponse = CustomerResponse(
        id = customer.id!!,
        name = customer.name,
        company = customer.company,
        title = customer.title,
        email = customer.email,
        phone = customer.phone,
        address = customer.address,
        status = customer.status,
        category = customer.category,
        createdAt = customer.createdAt,
        updatedAt = customer.updatedAt,
        createdBy = customer.createdBy
    )

    fun toEntity(request: CreateCustomerRequest): Customer = Customer(
        id = null,
        name = request.name,
        company = request.company,
        title = request.title ?: "",
        email = request.email ?: "",
        phone = request.phone ?: "",
        address = request.address ?: "",
        status = CustomerStatus.ACTIVE,
        category = request.category,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        createdBy = 0 // Will be set by service
    )

    fun updateEntity(request: UpdateCustomerRequest, customer: Customer): Customer = customer.copy(
        name = request.name ?: customer.name,
        company = request.company ?: customer.company,
        title = request.title ?: customer.title,
        email = request.email ?: customer.email,
        phone = request.phone ?: customer.phone,
        address = request.address ?: customer.address,
        status = request.status ?: customer.status,
        category = request.category ?: customer.category,
        updatedAt = LocalDateTime.now()
    )
}
