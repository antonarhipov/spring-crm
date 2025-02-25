package org.example.crm.util

import org.example.crm.user.domain.User
import org.example.crm.user.domain.UserRole
import org.example.crm.user.domain.UserStatus
import org.example.crm.customer.domain.Customer
import org.example.crm.customer.domain.CustomerCategory
import org.example.crm.customer.domain.CustomerStatus
import org.example.crm.contact.domain.Contact
import org.example.crm.contact.domain.CommunicationPreference
import java.time.LocalDateTime

object TestDataFactory {
    fun createUser(
        id: Long? = null,
        username: String = "testuser",
        email: String = "test@example.com",
        passwordHash: String = "hashed_password",
        role: UserRole = UserRole.SALES_REPRESENTATIVE,
        status: UserStatus = UserStatus.ACTIVE,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ) = User(
        id = id,
        username = username,
        email = email,
        passwordHash = passwordHash,
        role = role,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    fun createCustomer(
        id: Long? = null,
        name: String = "Test Customer",
        company: String = "Test Company",
        title: String = "CEO",
        email: String = "customer@example.com",
        phone: String = "+1234567890",
        address: String = "123 Test St",
        status: CustomerStatus = CustomerStatus.ACTIVE,
        category: CustomerCategory = CustomerCategory.PREMIUM,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now(),
        createdBy: Long = 1L
    ) = Customer(
        id = id,
        name = name,
        company = company,
        title = title,
        email = email,
        phone = phone,
        address = address,
        status = status,
        category = category,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy
    )

    fun createContact(
        id: Long? = null,
        customerId: Long = 1L,
        name: String = "Test Contact",
        position: String = "Sales Manager",
        email: String = "contact@example.com",
        phone: String = "+1234567890",
        communicationPreference: CommunicationPreference = CommunicationPreference.EMAIL,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now(),
        createdBy: Long = 1L
    ) = Contact(
        id = id,
        customerId = customerId,
        name = name,
        position = position,
        email = email,
        phone = phone,
        communicationPreference = communicationPreference,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy
    )
}