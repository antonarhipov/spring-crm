package org.example.crm.customer.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : CrudRepository<Customer, Long>, PagingAndSortingRepository<Customer, Long> {
    fun findByStatus(status: CustomerStatus, pageable: Pageable): Page<Customer>
    fun findByCategory(category: CustomerCategory, pageable: Pageable): Page<Customer>
    fun findByCreatedBy(userId: Long, pageable: Pageable): Page<Customer>
    
    fun findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrEmailContainingIgnoreCase(
        name: String,
        company: String,
        email: String,
        pageable: Pageable
    ): Page<Customer>
}