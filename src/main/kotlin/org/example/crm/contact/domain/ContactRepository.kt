package org.example.crm.contact.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : CrudRepository<Contact, Long>, PagingAndSortingRepository<Contact, Long> {
    fun findByCustomerId(customerId: Long, pageable: Pageable): Page<Contact>
    
    fun findByCustomerIdAndCommunicationPreference(
        customerId: Long,
        preference: CommunicationPreference,
        pageable: Pageable
    ): Page<Contact>
    
    fun findByCustomerIdAndNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
        customerId: Long,
        name: String,
        email: String,
        phone: String,
        pageable: Pageable
    ): Page<Contact>
    
    fun deleteByCustomerId(customerId: Long)
}