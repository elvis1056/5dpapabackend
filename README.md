# 5dpapa Backend

Backend API for 5dpapa project built with Spring Boot 3.

## Tech Stack

- **Java**: 17
- **Spring Boot**: 3.4.1
- **Database**: H2 (development), MySQL (production)
- **Security**: Spring Security + JWT
- **Build Tool**: Maven 3.9.11

## Project Structure

```
src/main/java/com/fivepapa/backend/
├── config/           # Configuration classes
├── entity/           # JPA entities
├── repository/       # Data access layer
├── service/          # Business logic
├── controller/       # REST controllers
├── dto/              # Data Transfer Objects
├── exception/        # Custom exceptions
├── security/         # Security configuration
└── util/             # Utility classes
```

## Features

### Implemented
- [ ] Member System
  - [ ] User registration
  - [ ] User login (JWT)
  - [ ] User profile management

### Planned
- [ ] Blog System
  - [ ] Create/Read/Update/Delete posts
  - [ ] Admin-only post creation
- [ ] Order System
  - [ ] Order management
  - [ ] Payment integration (future)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use included Maven Wrapper)

### Running the Application

```bash
# Using Maven Wrapper (recommended)
./mvnw spring-boot:run

# Or using Maven
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Console

Access H2 database console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

## API Documentation

See [docs/02_API_TESTING.md](./docs/02_API_TESTING.md) for API testing guide.

### Authentication Endpoints

```
POST /api/v1/auth/register  - Register new user
POST /api/v1/auth/login     - Login and get JWT token
POST /api/v1/auth/refresh   - Refresh access token
```

### User Endpoints

```
GET    /api/v1/users/{id}   - Get user by ID (requires auth)
PUT    /api/v1/users/{id}   - Update user (requires auth)
DELETE /api/v1/users/{id}   - Delete user (requires auth)
```

## Testing

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## Building for Production

```bash
# Build JAR file
./mvnw clean package

# Run JAR
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Environment Variables

Create `.env` file in project root (not committed to Git):

```properties
JWT_SECRET=your-secret-key-here-minimum-256-bits
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=604800000
```

## Development Workflow

1. Create feature branch: `git checkout -b feature/your-feature`
2. Make changes and commit: `git commit -m "feat: add your feature"`
3. Run tests: `./mvnw test`
4. Push and create PR: `git push origin feature/your-feature`

## Documentation

- [Development Standards](./docs/00_DEV_STANDARDS.md)
- [Database Basics](./docs/01_DATABASE_BASICS.md)
- [API Testing Guide](./docs/02_API_TESTING.md)
- [Unit Testing Guide](./docs/03_UNIT_TESTING.md)
- [Security Basics](./docs/04_SECURITY_BASICS.md)

## Frontend

Frontend repository: https://github.com/elvis1056/5dpapa

Deployed at: https://elvis1056.github.io/5dpapa/

## License

Private project

## Contributors

- Elvis (elvis1056)
