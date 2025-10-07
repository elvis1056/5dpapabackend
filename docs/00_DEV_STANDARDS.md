# Development Standards (開發規範)

**Last Updated**: 2025-10-07
**Purpose**: This document defines the mandatory standards I (Claude) must follow when developing this project.

---

## 1. Code Quality Standards

### 1.1 Code Style
- **Framework**: Spring Boot Official Style
- **Naming Convention**:
  - Class: `UpperCamelCase` (e.g., `UserService`)
  - Method: `lowerCamelCase` (e.g., `findUserById`)
  - Field: `lowerCamelCase` (e.g., `userRepository`)
  - Constant: `UPPER_SNAKE_CASE` (e.g., `MAX_LOGIN_ATTEMPTS`)
  - Package: lowercase (e.g., `com.fivepapa.backend.user`)

### 1.2 Comments
- **Language**: English
- **Required**:
  - JavaDoc for public methods
  - Inline comments for complex logic
  - TODO comments for incomplete features
- **Format**:
  ```java
  /**
   * Find user by ID
   * @param userId user's unique identifier
   * @return User entity
   * @throws UserNotFoundException if user not found
   */
  public User findUserById(Long userId) { ... }
  ```

### 1.3 Lombok Usage
- Use `@Builder` for entities with many fields
- Use `@RequiredArgsConstructor` for dependency injection
- Use `@Data` cautiously (prefer `@Getter`, `@Setter` separately)
- Always use `@NoArgsConstructor` and `@AllArgsConstructor` for JPA entities

---

## 2. Project Structure

```
src/main/java/com/fivepapa/backend/
├── config/           # Configuration classes
├── entity/           # JPA entities (database models)
├── repository/       # Data access layer (Spring Data JPA)
├── service/          # Business logic layer
├── controller/       # REST API controllers
├── dto/              # Data Transfer Objects (request/response)
├── exception/        # Custom exceptions
├── security/         # Security configurations
└── util/             # Utility classes
```

---

## 3. Development Workflow (Mandatory Steps)

### 3.1 Before Writing Code
- [ ] Check if the feature aligns with project requirements
- [ ] Search for existing similar implementations in the codebase
- [ ] Plan the database schema changes (if any)

### 3.2 When Writing Code
- [ ] Follow the project structure above
- [ ] Write JavaDoc comments for public methods
- [ ] Use meaningful variable names
- [ ] Keep methods small (< 30 lines recommended)
- [ ] Use Spring annotations correctly (`@Service`, `@RestController`, etc.)

### 3.3 After Writing Code (Critical - Must Do Every Time)
- [ ] Write unit tests for service layer
- [ ] Write integration tests for controller layer (if applicable)
- [ ] Manually test using Postman or curl
- [ ] Check for compilation errors: `mvn clean compile`
- [ ] Run all tests: `mvn test`
- [ ] Verify the application starts: `mvn spring-boot:run`
- [ ] Document how to test the feature manually

### 3.4 When Making Database Changes
- [ ] Update entity classes
- [ ] Verify H2 console works (during development)
- [ ] Document the schema changes in comments
- [ ] Plan migration strategy for production (when moving to MySQL)

---

## 4. Testing Standards

### 4.1 Unit Testing
- **Framework**: JUnit 5 + Mockito
- **Coverage**: Minimum 70% for service layer
- **Pattern**:
  ```java
  @Test
  void shouldFindUserById_WhenUserExists() {
      // Given (準備測試資料)
      Long userId = 1L;
      User mockUser = new User(userId, "elvis", "pass123");
      when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

      // When (執行測試)
      User result = userService.findUserById(userId);

      // Then (驗證結果)
      assertNotNull(result);
      assertEquals("elvis", result.getUsername());
  }
  ```

### 4.2 Integration Testing
- Test full API endpoints with `@SpringBootTest`
- Use `@AutoConfigureMockMvc` for controller testing
- Verify HTTP status codes and response bodies

### 4.3 Manual Testing Documentation
- Provide Postman collection or curl commands
- Include expected request/response examples
- Document edge cases and error scenarios

---

## 5. Security Standards

### 5.1 Password Security
- **Algorithm**: BCrypt (Spring Security default)
- **Strength**: BCrypt strength = 10
- Never store plain text passwords
- Never log passwords (even encrypted ones)

### 5.2 JWT Token
- **Token Type**: Access Token + Refresh Token
- **Access Token Expiry**: 1 hour
- **Refresh Token Expiry**: 7 days
- **Secret Key**: Store in `application.properties` (never hardcode)
- **Algorithm**: HS512

### 5.3 API Security
- All endpoints require authentication (except `/api/auth/register` and `/api/auth/login`)
- Use `@PreAuthorize` for role-based access control
- Validate all user inputs
- Return generic error messages (don't expose internal details)

---

## 6. Database Standards

### 6.1 Development Phase
- **Database**: H2 (in-memory)
- **Access**: H2 console at `http://localhost:8080/h2-console`
- **Auto-DDL**: `spring.jpa.hibernate.ddl-auto=create-drop`

### 6.2 Production Phase (Future)
- **Database**: MySQL 8.0+
- **Charset**: utf8mb4
- **Collation**: utf8mb4_unicode_ci
- **Auto-DDL**: `spring.jpa.hibernate.ddl-auto=validate`

### 6.3 Entity Design
- Always use `@Entity` and `@Table(name = "table_name")`
- Primary key: `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Use `@Column` to specify constraints
- Add `@CreatedDate` and `@LastModifiedDate` for auditing

---

## 7. API Design Standards

### 7.1 REST Conventions
- Use proper HTTP methods:
  - `GET`: Retrieve data
  - `POST`: Create new resource
  - `PUT`: Update entire resource
  - `PATCH`: Update partial resource
  - `DELETE`: Remove resource

### 7.2 URL Structure
```
/api/{version}/{resource}/{id}/{sub-resource}

Examples:
GET    /api/v1/users           # List all users
GET    /api/v1/users/1         # Get user by ID
POST   /api/v1/users           # Create user
PUT    /api/v1/users/1         # Update user
DELETE /api/v1/users/1         # Delete user
GET    /api/v1/users/1/posts   # Get user's posts
```

### 7.3 Response Format
```json
{
  "success": true,
  "data": { ... },
  "message": "Operation successful",
  "timestamp": "2025-10-07T12:00:00Z"
}
```

Error response:
```json
{
  "success": false,
  "error": "User not found",
  "message": "User with ID 999 does not exist",
  "timestamp": "2025-10-07T12:00:00Z"
}
```

---

## 8. Git Commit Standards

### 8.1 Commit Message Format
```
<type>: <subject>

<body>

Examples:
feat: add user registration endpoint
fix: resolve null pointer exception in login service
docs: update API documentation
test: add unit tests for UserService
refactor: simplify password validation logic
```

### 8.2 Commit Types
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `test`: Adding or updating tests
- `refactor`: Code refactoring
- `style`: Code style changes (formatting)
- `chore`: Build/dependency updates

---

## 9. Documentation Requirements

### 9.1 When Creating New Features
- Update this standards document if new patterns are introduced
- Create tutorial documents for complex concepts
- Provide step-by-step testing guide

### 9.2 Knowledge Base Documents
Maintain these tutorial documents in `/docs`:
- `DATABASE_BASICS.md` - Database concepts
- `API_TESTING.md` - How to test APIs
- `UNIT_TESTING.md` - Unit testing guide
- `SECURITY_BASICS.md` - Security concepts
- `DEPLOYMENT.md` - Deployment guide (future)

---

## 10. Validation Checklist (Use Before Every Commit)

- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] Application starts successfully
- [ ] API endpoints work as expected (tested manually)
- [ ] No sensitive data in code (passwords, API keys)
- [ ] Comments and documentation updated
- [ ] No unused imports or variables
- [ ] Followed naming conventions
- [ ] Created/updated tests
- [ ] Created manual testing guide

---

## 11. Communication Standards

### 11.1 When Presenting Code Changes
- Explain what was changed and why
- Provide file paths and line numbers for reference
- Show before/after comparisons for major changes

### 11.2 When Asking for Clarification
- Ask specific questions
- Provide context and examples
- Suggest alternatives when possible

### 11.3 When User is a Beginner
- Explain concepts before implementing
- Create tutorial documents proactively
- Provide multiple examples
- Verify understanding before proceeding

---

## 12. Important Reminders

### Things I Must NEVER Do
- ❌ Skip unit testing
- ❌ Hardcode sensitive data (passwords, tokens, API keys)
- ❌ Create code without verifying it compiles
- ❌ Use deprecated Spring Boot features
- ❌ Ignore user's questions or proceed without confirmation
- ❌ Create unnecessary files
- ❌ Use outdated libraries

### Things I Must ALWAYS Do
- ✅ Write tests before marking a feature complete
- ✅ Provide manual testing instructions
- ✅ Check for compilation errors
- ✅ Follow project structure
- ✅ Use Lombok to reduce boilerplate
- ✅ Document complex logic
- ✅ Ask for clarification when requirements are unclear
- ✅ Create tutorial documents for new concepts

---

**This document is a living document. Update it whenever new standards or patterns are established.**
