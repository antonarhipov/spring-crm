# Spring CRM System

A Customer Relationship Management system built with Spring Boot and Kotlin, following clean architecture principles and domain-driven design.

## Features

- User Management
- Customer Management
- Contact Management
- Role-based Access Control
- RESTful API
- Comprehensive Test Coverage

## Technology Stack

- Kotlin 1.9.x
- Spring Boot 3.2.x
- Spring Data JDBC
- PostgreSQL
- Docker & Docker Compose
- JWT Authentication

## Prerequisites

- JDK 17 or later
- Docker and Docker Compose
- Gradle 8.x

## Getting Started

### Development Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/spring-crm.git
cd spring-crm
```

2. Start the PostgreSQL database using Docker Compose:
```bash
./gradlew dockerComposeUp
```

3. Run the application in development mode:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The application will be available at `http://localhost:8080`

### Running Tests

```bash
./gradlew test
```

### API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/api-docs`

## Project Structure

```
src/
├── main/
│   └── kotlin/
│       └── org/example/crm/
│           ├── user/
│           │   ├── api/
│           │   ├── domain/
│           │   ├── mapping/
│           │   └── service/
│           ├── customer/
│           │   ├── api/
│           │   ├── domain/
│           │   ├── mapping/
│           │   └── service/
│           ├── contact/
│           │   ├── api/
│           │   ├── domain/
│           │   ├── mapping/
│           │   └── service/
│           └── shared/
│               ├── config/
│               ├── error/
│               └── security/
└── test/
    └── kotlin/
        └── org/example/crm/
            ├── user/
            ├── customer/
            └── contact/
```

## Development Guidelines

### Code Style

- Follow Kotlin coding conventions
- Use meaningful names for classes, methods, and variables
- Write comprehensive unit tests
- Document public APIs

### Git Workflow

1. Create a feature branch from `main`
2. Make your changes
3. Write or update tests
4. Submit a pull request

## Docker Compose

The project includes a Docker Compose configuration for development:

```yaml
services:
  postgres:
    image: postgres:latest
    environment:
      POSTGRES_DB: crm
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
```

### Docker Compose Commands

- Start services: `./gradlew dockerComposeUp`
- Stop services: `./gradlew dockerComposeDown`
- View logs: `./gradlew dockerComposeLogs`

## Configuration

### Application Properties

The application uses different property files for different environments:

- `application.properties`: Common configuration
- `application-dev.properties`: Development configuration
- `application-test.properties`: Test configuration

### Environment Variables

- `JWT_SECRET`: JWT signing key (required in production)
- `POSTGRES_PASSWORD`: Database password (required in production)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.