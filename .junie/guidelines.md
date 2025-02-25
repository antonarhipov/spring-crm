# Spring Boot with Kotlin Development Guidelines

This document provides comprehensive guidelines for building Spring Boot applications with Kotlin, focusing on CRUD functionality, clean architecture, and test-driven development.

## Table of Contents
1. [Technology Stack](#technology-stack)
2. [Project Structure](#project-structure)
3. [Implementation Guidelines](#implementation-guidelines)
4. [Testing Strategy](#testing-strategy)
5. [Best Practices](#best-practices)

## Technology Stack

### Core Technologies
- Kotlin 1.9.x
- Spring Boot 3.x
- Spring Data JDBC
- MapStruct
- TestContainers

### Build Configuration

```kotlin
plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("kapt") version "1.9.20"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    
    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")
    
    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}
```

## Project Structure

Follow a domain-driven package structure:

```
src/
├── main/
│   └── kotlin/
│       └── com/company/project/
│           ├── domain1/
│           │   ├── api/
│           │   ├── domain/
│           │   ├── mapping/
│           │   └── service/
│           └── shared/
│               ├── config/
│               └── error/
└── test/
    └── kotlin/
        └── com/company/project/
            └── domain1/
                ├── api/
                ├── integration/
                └── service/
```

## Implementation Guidelines

### 1. Domain Entity

```kotlin
@Table("domain_items")
data class DomainEntity(
    @Id val id: Long? = null,
    val field1: String,
    val field2: Int
)
```

### 2. Repository Interface

```kotlin
@Repository
interface DomainRepository : CrudRepository<DomainEntity, Long> {
    fun findByField1(field1: String): List<DomainEntity>
}
```

### 3. DTO

```kotlin
data class DomainDto(
    val id: Long?,
    val field1: String,
    val field2: Int
)
```

### 4. Mapper

```kotlin
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
interface DomainMapper {
    fun toDto(entity: DomainEntity): DomainDto
    fun toEntity(dto: DomainDto): DomainEntity
    fun updateEntity(dto: DomainDto, @MappingTarget entity: DomainEntity): DomainEntity
}
```

### 5. Service Layer

```kotlin
@Service
class DomainService(
    private val repository: DomainRepository,
    private val mapper: DomainMapper
) {
    @Transactional(readOnly = true)
    fun findById(id: Long): DomainDto? =
        repository.findById(id)
            .map(mapper::toDto)
            .orElse(null)

    @Transactional
    fun create(dto: DomainDto): DomainDto =
        mapper.toEntity(dto)
            .let(repository::save)
            .let(mapper::toDto)

    @Transactional
    fun update(id: Long, dto: DomainDto): DomainDto? =
        repository.findById(id)
            .map { entity -> mapper.updateEntity(dto, entity) }
            .map(repository::save)
            .map(mapper::toDto)
            .orElse(null)

    @Transactional
    fun delete(id: Long) = repository.deleteById(id)
}
```

### 6. Controller

```kotlin
@RestController
@RequestMapping("/api/v1/domains")
class DomainController(
    private val service: DomainService
) {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): DomainDto =
        service.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: DomainDto): DomainDto =
        service.create(dto)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: DomainDto): DomainDto =
        service.update(id, dto) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) =
        service.delete(id)
}
```

## Testing Strategy

### Integration Tests with TestContainers

```kotlin
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DomainIntegrationTest {
    companion object {
        @Container
        private val postgres = PostgreSQLContainer("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Test
    fun `should create and retrieve domain entity`() {
        // Test implementation
    }
}
```

## Best Practices

### 1. Test-First Development
- Write integration tests before implementation
- Use TestContainers for database testing
- Minimize mocking, prefer real dependencies

### 2. Package Structure
- Organize by domain rather than technical layers
- Keep related code together
- Use shared packages for common components

### 3. Error Handling

```kotlin
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
        return when (ex) {
            is EntityNotFoundException -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse(message = ex.message))
            else -> ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse(message = "Internal server error"))
        }
    }
}
```

### 4. Database Operations
- Use appropriate transaction boundaries
- Leverage read-only transactions where possible
- Use Spring Data JDBC for simple and efficient persistence

### 5. API Design
- Use DTOs to control API contracts
- Follow REST conventions
- Version your APIs
- Document with OpenAPI/Swagger

### 6. Development Workflow
1. Write integration test
2. Implement domain model
3. Create repository
4. Implement service layer
5. Create controller
6. Verify tests
7. Document API

### 7. Code Quality
- Follow SOLID principles
- Use meaningful names
- Keep methods small and focused
- Regular refactoring
- Proper logging
- Security best practices

### 8. Configuration
- Use configuration properties classes
- Externalize configuration
- Use profiles for different environments

```kotlin
@ConfigurationProperties(prefix = "app")
data class AppProperties(
    val feature1Enabled: Boolean = false,
    val apiKey: String? = null
)
```

### 9. Security Considerations
- Validate input
- Use HTTPS
- Implement proper authentication
- Follow OWASP guidelines

### 10. Kotlin Idiomatic Style Guidelines

#### 10.1. Naming Conventions
- Use camelCase for properties, variables, functions, and parameters
- Use PascalCase for classes and interfaces
- Use SCREAMING_SNAKE_CASE for constants
- Prefer meaningful and descriptive names over abbreviations

#### 10.2. Function Design
- Prefer expression bodies for simple functions
```kotlin
// Good
fun double(x: Int) = x * 2

// Avoid if simple
fun double(x: Int) {
    return x * 2
}
```
- Use named arguments for better readability with multiple parameters
```kotlin
createUser(name = "John", age = 25, email = "john@example.com")
```
- Use extension functions deliberately:
    - Make it a member function if it's essential to the class's core functionality
    - Make it an extension function if it's a utility or enhancement
```kotlin
// Good: Essential functionality as member function
class User(val name: String) {
    fun validateCredentials(): Boolean {  // Core functionality
        // validation logic
    }
}

// Good: Utility functionality as extension function
fun User.toDisplayString(): String {  // Enhancement, not core functionality
    return "${name.capitalize()}"
}

// Bad: Core functionality as extension
fun User.validateCredentials(): Boolean {  // Should be a member function
    // validation logic
}

// Bad: Utility as member
class User(val name: String) {
    fun toDisplayString(): String {  // Should be an extension
        return "${name.capitalize()}"
    }
}
```
- Utilize extension functions to extend functionality cleanly
```kotlin
fun String.toTitleCase(): String = ...
```

#### 10.3. Null Safety
- Leverage Kotlin's null safety features
- Use nullable types only when necessary
- Prefer `?.` (safe call) over `!!` (non-null assertion)
- Use Elvis operator `?:` for default values
```kotlin
val length = str?.length ?: 0
```

### 4. Smart Casts and Type Checks
- Use `when` with `is` checks to leverage smart casts
```kotlin
when (obj) {
    is String -> obj.length
    is List<*> -> obj.size
    else -> null
}
```

#### 10.5. Collections
- Use immutable collections by default (List, Set, Map)
- Use sequence for large collections with multiple operations
- Prefer collection operations over loops for better readability
```kotlin
// Good
val adults = people.filter { it.age >= 18 }.map { it.name }

// Avoid
val adults = mutableListOf<String>()
for (person in people) {
    if (person.age >= 18) {
        adults.add(person.name)
    }
}
```

#### 10.6. Classes and Properties
- Use data classes for DTOs and value objects with these rules:
    - Use only immutable properties (`val`, not `var`)
    - Do not use companion objects in data classes
    - Avoid private constructors
    - Validate parameters in the `init{}` block
    - Use data classes strictly as immutable data holders
```kotlin
// Good
data class User(
    val name: String,
    val age: Int,
    val email: String
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(age >= 0) { "Age cannot be negative" }
        require(email.contains("@")) { "Invalid email format" }
    }
}

// Avoid
data class User(
    var name: String,  // Bad: mutable property
    val age: Int
) {
    companion object {  // Bad: companion object in data class
        fun create(name: String) = User(name, 0)
    }
}
```
- Use object for singletons
- Use companion objects for factory methods and static members (in regular classes, not data classes)

#### 10.7. Coroutines
- Use structured concurrency
- Prefer suspending functions over callbacks
- Use appropriate dispatchers
- Handle exceptions properly with try-catch or supervisorScope

#### 10.8. Anti-patterns to Avoid
1. Overuse of `!!` operator
2. Unnecessary use of nullable types
3. Using Java-style static methods instead of companion objects
4. Excessive use of inheritance over composition
5. Not leveraging Kotlin's standard library functions
6. Using var when val would suffice
7. Ignoring coroutine scope and context
8. Using Java-style loops instead of functional operations

#### 10.9. Best Practices
1. Use sealed classes for representing restricted hierarchies
2. Leverage inline functions for high-order functions
3. Use object declarations for singletons
4. Utilize property delegation when appropriate
5. Use scope functions (let, run, with, apply, also) appropriately
6. Write unit tests using Kotlin-specific testing libraries
7. Use backing properties when needed
8. Implement proper exception handling

#### 10.10. Code Organization
1. Group related functionality into packages
2. Keep files focused and single-purpose
3. Use extension functions to organize utility functions
4. Separate business logic from UI/framework code
5. Follow clean architecture principles
