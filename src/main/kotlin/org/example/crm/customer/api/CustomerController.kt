package org.example.crm.customer.api

import jakarta.validation.Valid
import org.example.crm.customer.api.dto.CreateCustomerRequest
import org.example.crm.customer.api.dto.CustomerResponse
import org.example.crm.customer.api.dto.UpdateCustomerRequest
import org.example.crm.customer.domain.CustomerCategory
import org.example.crm.customer.domain.CustomerStatus
import org.example.crm.customer.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    fun createCustomer(
        @Valid @RequestBody request: CreateCustomerRequest,
        @AuthenticationPrincipal user: UserDetails
    ): CustomerResponse = customerService.createCustomer(request, user.username.toLong())

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun getCustomer(@PathVariable id: Long): CustomerResponse =
        customerService.getCustomer(id)

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    fun updateCustomer(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateCustomerRequest
    ): CustomerResponse = customerService.updateCustomer(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER')")
    fun deleteCustomer(@PathVariable id: Long) =
        customerService.deleteCustomer(id)

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun listCustomers(
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<CustomerResponse> = customerService.listCustomers(pageable)

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun searchCustomers(
        @RequestParam query: String,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<CustomerResponse> = customerService.searchCustomers(query, pageable)

    @GetMapping("/by-status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun findByStatus(
        @PathVariable status: CustomerStatus,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<CustomerResponse> = customerService.findByStatus(status, pageable)

    @GetMapping("/by-category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_MANAGER', 'SALES_REPRESENTATIVE', 'READ_ONLY')")
    fun findByCategory(
        @PathVariable category: CustomerCategory,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<CustomerResponse> = customerService.findByCategory(category, pageable)

    @GetMapping("/my-customers")
    @PreAuthorize("hasAnyRole('SALES_MANAGER', 'SALES_REPRESENTATIVE')")
    fun findMyCustomers(
        @AuthenticationPrincipal user: UserDetails,
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ): Page<CustomerResponse> = customerService.findByCreatedBy(user.username.toLong(), pageable)
}