package org.example.crm.contact.mapping

import org.example.crm.contact.api.dto.ContactDto
import org.example.crm.contact.api.dto.ContactResponse
import org.example.crm.contact.api.dto.CreateContactRequest
import org.example.crm.contact.api.dto.UpdateContactRequest
import org.example.crm.contact.domain.Contact
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface ContactMapper {
    fun toDto(contact: Contact): ContactDto
    
    fun toResponse(contact: Contact): ContactResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    fun toEntity(request: CreateContactRequest): Contact

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    fun updateEntity(request: UpdateContactRequest, @MappingTarget contact: Contact): Contact
}