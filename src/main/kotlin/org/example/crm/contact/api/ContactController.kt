package org.example.crm.contact.api

import jakarta.validation.Valid
import org.example.crm.contact.api.dto.ContactResponse
import org.example.crm.contact.api.dto.CreateContactRequest
import org.example.crm.contact.api.dto.UpdateContactRequest
import org.example.crm.contact.domain.CommunicationPreference
import org.example.crm.contact.service.ContactService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customers/{customerId}/contacts")
class ContactController(
    private val contactService: ContactService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    fun createContact(
        @PathVariable customerId: Long,
        @Valid @RequestBody request: CreateContactRequest,
        @AuthenticationPrincipal user: UserDetails
    ): ContactResponse = contactService.createContact(
        request.copy(customerId = customerId),
        user.username.toLong()
    )

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun getContact(
        @PathVariable customerId: Long,
        @PathVariable id: Long
    ): ContactResponse = contactService.getContact(id)

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    fun updateContact(
        @PathVariable customerId: Long,
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateContactRequest
    ): ContactResponse = contactService.updateContact(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    fun deleteContact(
        @PathVariable customerId: Long,
        @PathVariable id: Long
    ) = contactService.deleteContact(id)

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun listContacts(
        @PathVariable customerId: Long,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<ContactResponse> = contactService.listContacts(customerId, pageable)

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun searchContacts(
        @PathVariable customerId: Long,
        @RequestParam query: String,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<ContactResponse> = contactService.searchContacts(customerId, query, pageable)

    @GetMapping("/by-preference/{preference}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun findByPreference(
        @PathVariable customerId: Long,
        @PathVariable preference: CommunicationPreference,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<ContactResponse> = contactService.findByCustomerIdAndCommunicationPreference(
        customerId,
        preference,
        pageable
    )
}