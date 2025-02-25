package org.example.crm.customer.service

import org.example.crm.customer.api.dto.CreateCustomerRequest
import org.example.crm.customer.api.dto.CustomerResponse
import org.example.crm.customer.api.dto.UpdateCustomerRequest
import org.example.crm.customer.domain.CustomerCategory
import org.example.crm.customer.domain.CustomerStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomerService {
    fun createCustomer(request: CreateCustomerRequest, createdBy: Long): CustomerResponse
    fun getCustomer(id: Long): CustomerResponse
    fun updateCustomer(id: Long, request: UpdateCustomerRequest): CustomerResponse
    fun deleteCustomer(id: Long)
    fun listCustomers(pageable: Pageable): Page<CustomerResponse>
    fun findByStatus(status: CustomerStatus, pageable: Pageable): Page<CustomerResponse>
    fun findByCategory(category: CustomerCategory, pageable: Pageable): Page<CustomerResponse>
    fun findByCreatedBy(userId: Long, pageable: Pageable): Page<CustomerResponse>
    fun searchCustomers(query: String, pageable: Pageable): Page<CustomerResponse>
}