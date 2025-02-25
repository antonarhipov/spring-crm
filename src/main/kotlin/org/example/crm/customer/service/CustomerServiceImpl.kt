package org.example.crm.customer.service

import org.example.crm.customer.api.dto.CreateCustomerRequest
import org.example.crm.customer.api.dto.CustomerResponse
import org.example.crm.customer.api.dto.UpdateCustomerRequest
import org.example.crm.customer.domain.CustomerCategory
import org.example.crm.customer.domain.CustomerRepository
import org.example.crm.customer.domain.CustomerStatus
import org.example.crm.customer.mapping.CustomerMapper
import org.example.crm.shared.error.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val customerMapper: CustomerMapper
) : CustomerService {

    @Transactional
    override fun createCustomer(request: CreateCustomerRequest, createdBy: Long): CustomerResponse {
        val customer = customerMapper.toEntity(request).copy(createdBy = createdBy)
        return customerMapper.toResponse(customerRepository.save(customer))
    }

    @Transactional(readOnly = true)
    override fun getCustomer(id: Long): CustomerResponse =
        customerRepository.findById(id)
            .map(customerMapper::toResponse)
            .orElseThrow { EntityNotFoundException("Customer not found with id: $id") }

    @Transactional
    override fun updateCustomer(id: Long, request: UpdateCustomerRequest): CustomerResponse {
        val customer = customerRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Customer not found with id: $id") }

        val updatedCustomer = customerMapper.updateEntity(request, customer)
        return customerMapper.toResponse(customerRepository.save(updatedCustomer))
    }

    @Transactional
    override fun deleteCustomer(id: Long) {
        if (!customerRepository.existsById(id)) {
            throw EntityNotFoundException("Customer not found with id: $id")
        }
        customerRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun listCustomers(pageable: Pageable): Page<CustomerResponse> =
        customerRepository.findAll(pageable).map(customerMapper::toResponse)

    @Transactional(readOnly = true)
    override fun findByStatus(status: CustomerStatus, pageable: Pageable): Page<CustomerResponse> =
        customerRepository.findByStatus(status, pageable).map(customerMapper::toResponse)

    @Transactional(readOnly = true)
    override fun findByCategory(category: CustomerCategory, pageable: Pageable): Page<CustomerResponse> =
        customerRepository.findByCategory(category, pageable).map(customerMapper::toResponse)

    @Transactional(readOnly = true)
    override fun findByCreatedBy(userId: Long, pageable: Pageable): Page<CustomerResponse> =
        customerRepository.findByCreatedBy(userId, pageable).map(customerMapper::toResponse)

    @Transactional(readOnly = true)
    override fun searchCustomers(query: String, pageable: Pageable): Page<CustomerResponse> =
        customerRepository
            .findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrEmailContainingIgnoreCase(
                name = query,
                company = query,
                email = query,
                pageable = pageable
            )
            .map(customerMapper::toResponse)
}