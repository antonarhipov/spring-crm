package org.example.crm.customer.api.dto

import jakarta.validation.constraints.*
import org.example.crm.customer.domain.CustomerCategory
import org.example.crm.customer.domain.CustomerStatus
import java.time.LocalDateTime

data class CustomerDto(
    val id: Long? = null,
    val name: String,
    val company: String,
    val title: String,
    val email: String,
    val phone: String,
    val address: String,
    val status: CustomerStatus,
    val category: CustomerCategory,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: Long
)

data class CreateCustomerRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    @field:NotBlank(message = "Company is required")
    @field:Size(min = 2, max = 100, message = "Company must be between 2 and 100 characters")
    val company: String,

    @field:Size(max = 100, message = "Title cannot exceed 100 characters")
    val title: String? = null,

    @field:Email(message = "Invalid email format")
    val email: String? = null,

    @field:Pattern(regexp = "^\\+?[1-9]\\d{1,14}\$", message = "Invalid phone number format")
    val phone: String? = null,

    val address: String? = null,

    @field:NotNull(message = "Category is required")
    val category: CustomerCategory
)

data class UpdateCustomerRequest(
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String? = null,

    @field:Size(min = 2, max = 100, message = "Company must be between 2 and 100 characters")
    val company: String? = null,

    @field:Size(max = 100, message = "Title cannot exceed 100 characters")
    val title: String? = null,

    @field:Email(message = "Invalid email format")
    val email: String? = null,

    @field:Pattern(regexp = "^\\+?[1-9]\\d{1,14}\$", message = "Invalid phone number format")
    val phone: String? = null,

    val address: String? = null,

    val status: CustomerStatus? = null,

    val category: CustomerCategory? = null
)

data class CustomerResponse(
    val id: Long,
    val name: String,
    val company: String,
    val title: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val status: CustomerStatus,
    val category: CustomerCategory,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: Long
)