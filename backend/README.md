# BizGen AI - Backend

A template-driven content generation system for SMB owners built with Spring Boot.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.2+**
- **Spring Web MVC**
- **Spring Validation**
- **Spring Data JPA**
- **H2 Database** (in-memory with file persistence)
- **Lombok**
- **MapStruct** (DTO mapping)
- **Jackson** (JSON processing)
- **JUnit 5 + Mockito** (testing)
- **OpenAPI/Swagger** (documentation)

## Project Structure

```
src/main/java/com/bizgenai/
├── BizGenAiApplication.java          # Main application entry point
├── config/                            # Configuration classes
├── controller/                        # REST API controllers
├── dto/                              # Data Transfer Objects
│   ├── request/                      # Request DTOs
│   ├── response/                     # Response DTOs
│   └── mapper/                       # MapStruct mappers
├── entity/                           # JPA entities
├── repository/                       # Data repositories
├── service/                          # Business logic services
├── ai/                               # AI service abstraction
├── guardrail/                        # Content guardrails
│   └── rules/                        # Individual guardrail rules
├── blueprint/                        # Blueprint parsing/building
├── exception/                        # Exception handling
├── validation/                       # Input validation
└── util/                             # Utility classes

src/main/resources/
├── application.yml                   # Main configuration
├── application-dev.yml               # Development config
├── application-prod.yml              # Production config
├── data.sql                          # Seed data
├── blueprints/                       # Blueprint JSON files
│   ├── marketing/
│   └── legal/
└── domain-knowledge/                 # Domain knowledge JSON files
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+

### Running the Application

```bash
# Navigate to backend directory
cd backend

# Run with Maven
./mvnw spring-boot:run

# Or build and run
./mvnw clean package
java -jar target/bizgen-ai-1.0.0.jar
```

### Running Tests

```bash
./mvnw test
```

## API Endpoints

Base URL: `http://localhost:8080/api/v1`

### Categories
- `GET /categories` - Get all categories
- `GET /categories/{id}` - Get category by ID

### Templates
- `GET /templates` - Get all templates
- `GET /templates?categoryId={id}` - Get templates by category
- `GET /templates/{id}` - Get template by ID
- `GET /templates/{id}/schema` - Get template form schema

### Generation
- `POST /generate` - Generate content
- `GET /generate/{generationId}` - Get generation by ID

### History
- `GET /history` - Get generation history (paginated)
- `GET /history/{id}` - Get history item
- `DELETE /history/{id}` - Delete history item

### Health
- `GET /health` - Health check

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`
OpenAPI Spec: `http://localhost:8080/api-docs`

## Database Console

H2 Console (dev only): `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/bizgenai`
- Username: `sa`
- Password: (empty)

## Blueprint Structure

Blueprints define the form schema and AI prompt configuration for each template. See `src/main/resources/blueprints/` for examples.

## AI Service

The AI service is abstracted via the `AiService` interface. Currently, a `MockAiService` is used for development. To integrate a real AI provider:

1. Implement the `AiService` interface
2. Configure it for the `prod` profile
3. Add necessary API credentials to configuration

## License

MIT
