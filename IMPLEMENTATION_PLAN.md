# CRM System Implementation Plan

## Phase 1: Core Features Implementation

### 1. Project Setup
1. Configure build.gradle.kts with required dependencies
2. Set up domain-driven package structure:
   ```
   src/main/kotlin/org/example/crm/
   ├── user/
   │   ├── api/
   │   ├── domain/
   │   ├── mapping/
   │   └── service/
   ├── customer/
   │   ├── api/
   │   ├── domain/
   │   ├── mapping/
   │   └── service/
   ├── contact/
   │   ├── api/
   │   ├── domain/
   │   ├── mapping/
   │   └── service/
   └── shared/
       ├── config/
       ├── security/
       └── error/
   ```
3. Configure PostgreSQL database
4. Set up basic Spring Boot configuration

### 2. Domain Models

#### User Domain
```kotlin
@Table("users")
data class User(
    @Id val id: Long? = null,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val status: UserStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

enum class UserRole {
    ADMIN, SALES_MANAGER, SALES_REPRESENTATIVE, READ_ONLY
}

enum class UserStatus {
    ACTIVE, INACTIVE
}
```

#### Customer Domain
```kotlin
@Table("customers")
data class Customer(
    @Id val id: Long? = null,
    val name: String,
    val company: String,
    val title: String,
    val email: String,
    val phone: String,
    val address: String,
    val status: CustomerStatus,
    val category: CustomerCategory,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: Long
)

enum class CustomerStatus {
    ACTIVE, INACTIVE
}

enum class CustomerCategory {
    REGULAR, PREMIUM, ENTERPRISE
}
```

#### Contact Domain
```kotlin
@Table("contacts")
data class Contact(
    @Id val id: Long? = null,
    val customerId: Long,
    val name: String,
    val position: String,
    val email: String,
    val phone: String,
    val communicationPreference: CommunicationPreference,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: Long
)

enum class CommunicationPreference {
    EMAIL, PHONE, BOTH
}
```

### 3. Database Schema
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    address TEXT,
    status VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE contacts (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    name VARCHAR(255) NOT NULL,
    position VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    communication_preference VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by BIGINT NOT NULL REFERENCES users(id)
);
```

### 4. API Endpoints

#### User Management
- POST /api/v1/users - Create user
- GET /api/v1/users/{id} - Get user by ID
- PUT /api/v1/users/{id} - Update user
- DELETE /api/v1/users/{id} - Delete user
- POST /api/v1/auth/login - User authentication

#### Customer Management
- POST /api/v1/customers - Create customer
- GET /api/v1/customers/{id} - Get customer by ID
- GET /api/v1/customers - List customers with filtering
- PUT /api/v1/customers/{id} - Update customer
- DELETE /api/v1/customers/{id} - Delete customer

#### Contact Management
- POST /api/v1/customers/{customerId}/contacts - Create contact
- GET /api/v1/customers/{customerId}/contacts/{id} - Get contact
- GET /api/v1/customers/{customerId}/contacts - List contacts
- PUT /api/v1/customers/{customerId}/contacts/{id} - Update contact
- DELETE /api/v1/customers/{customerId}/contacts/{id} - Delete contact

### 5. Implementation Order

1. Security Configuration
   - JWT authentication
   - Password encryption
   - Role-based authorization

2. User Management
   - User entity and repository
   - UserService implementation
   - UserController with endpoints
   - Authentication controller

3. Customer Management
   - Customer entity and repository
   - CustomerService implementation
   - CustomerController with endpoints

4. Contact Management
   - Contact entity and repository
   - ContactService implementation
   - ContactController with endpoints

5. Error Handling
   - Global exception handler
   - Custom exceptions
   - Error response DTOs

### 6. Testing Strategy

1. Unit Tests
   - Service layer tests
   - Controller tests
   - Repository tests

2. Integration Tests
   - API endpoint tests with TestContainers
   - Authentication flow tests
   - Database interaction tests

3. Test Data
   - Test data factories
   - Database seeders for testing

### 7. Documentation

1. API Documentation
   - OpenAPI/Swagger configuration
   - API endpoint documentation
   - Request/response examples

2. Technical Documentation
   - Setup instructions
   - Development guidelines
   - Database schema documentation

### 8. Monitoring and Logging

1. Basic Monitoring
   - Health check endpoints
   - Basic metrics collection

2. Logging
   - Request/response logging
   - Error logging
   - Audit logging for important operations

## Success Criteria for Phase 1

1. All Phase 1 features implemented and tested
2. Test coverage > 80%
3. API documentation complete
4. Basic security measures implemented
5. All integration tests passing
6. Clean code following Kotlin best practices
7. Proper error handling implemented