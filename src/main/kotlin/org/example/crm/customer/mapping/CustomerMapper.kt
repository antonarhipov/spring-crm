package org.example.crm.customer.mapping

import org.example.crm.customer.api.dto.CreateCustomerRequest
import org.example.crm.customer.api.dto.CustomerDto
import org.example.crm.customer.api.dto.CustomerResponse
import org.example.crm.customer.api.dto.UpdateCustomerRequest
import org.example.crm.customer.domain.Customer
import org.example.crm.customer.domain.CustomerStatus
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface CustomerMapper {
    fun toDto(customer: Customer): CustomerDto
    
    fun toResponse(customer: Customer): CustomerResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    fun toEntity(request: CreateCustomerRequest): Customer

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    fun updateEntity(request: UpdateCustomerRequest, @MappingTarget customer: Customer): Customer
}