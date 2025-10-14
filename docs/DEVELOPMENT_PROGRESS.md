# Development Progress - 5dpapa Backend

**Last Updated**: 2025-10-07
**Current Phase**: Phase 2 - 依賴設定與基礎配置
**Current Task**: 設定 application.properties
**Branch**: feature/member-system

---

## 📊 Overall Progress

- **已完成**: 8/48 (17%)
- **進行中**: 1/48 (2%)
- **待完成**: 39/48 (81%)

---

## ✅ Phase 1: 環境設定與專案初始化（已完成）

### 1. ✅ 驗證 Java 17 安裝
- **狀態**: 已完成
- **執行時間**: 2025-10-07
- **說明**:
  - 確認系統有 Java 17
  - 解決 SDKMAN 環境變數問題
  - Java 版本: 17.0.9 (Temurin)

### 2. ✅ 驗證 Maven Wrapper
- **狀態**: 已完成
- **執行時間**: 2025-10-07
- **說明**:
  - 測試 `./mvnw --version` 可以正常執行
  - Maven 版本: 3.9.11

### 3. ✅ 清理編譯產物
- **狀態**: 已完成
- **執行時間**: 2025-10-07
- **說明**:
  - 刪除 `target/` 目錄

### 4. ✅ 更新 .gitignore
- **狀態**: 已完成
- **執行時間**: 2025-10-07
- **說明**:
  - 新增 macOS 檔案排除 (.DS_Store)
  - 新增環境變數檔案排除 (.env)
  - 新增 log 檔案排除
  - 新增臨時檔案排除

### 5. ✅ 初始化 Git repository
- **狀態**: 已完成
- **執行時間**: 2025-10-07
- **說明**:
  - `git init`
  - 重新命名分支為 `main`

### 6. ✅ 建立 README.md
- **狀態**: 已完成
- **執行時間**: 2025-10-07
- **檔案位置**: `/README.md`
- **內容**:
  - 專案說明
  - 技術棧
  - 如何啟動
  - API 文件連結
  - 開發流程

### 7. ✅ 第一次 Git commit
- **狀態**: 已完成
- **執行時間**: 2025-10-07
- **Commit hash**: fe761c5
- **Commit message**: `chore: initial project setup with documentation`
- **包含檔案**:
  - .gitattributes
  - .gitignore
  - README.md
  - docs/ (所有教學文件)
  - pom.xml
  - src/main/java/com/fivepapa/backend/Application.java
  - src/main/resources/application.properties

### 8. ✅ 建立 feature/member-system 分支
- **狀態**: 已完成
- **執行時間**: 2025-10-07
- **分支名稱**: `feature/member-system`
- **說明**: `git checkout -b feature/member-system`

---

## ⏳ Phase 2: 依賴設定與基礎配置（進行中）

### 9. ⏳ 設定 application.properties
- **狀態**: 進行中 ← **目前在這裡**
- **檔案位置**: `/src/main/resources/application.properties`
- **要新增的設定**:
  - H2 資料庫設定
  - JPA 設定
  - H2 Console 啟用
  - JWT 設定（secret, expiration）
  - Server port
  - Logging 設定

### 10. ⏸️ 更新 pom.xml 啟用依賴
- **狀態**: 待完成
- **檔案位置**: `/pom.xml`
- **要新增的依賴**:
  - 啟用 Spring Data JPA
  - 新增 H2 database
  - 啟用 Spring Security
  - 新增 JWT 依賴 (jjwt-api 0.12.3, jjwt-impl 0.12.3, jjwt-jackson 0.12.3)

### 11. ⏸️ 驗證專案可以編譯
- **狀態**: 待完成
- **指令**: `./mvnw clean compile`
- **驗證項目**: 確認沒有編譯錯誤

### 12. ⏸️ Commit: 設定完成
- **狀態**: 待完成
- **預計 Commit message**: `build: configure dependencies and application properties`
- **包含變更**:
  - pom.xml
  - application.properties

---

## ⏸️ Phase 3: 專案結構建立

### 13. ⏸️ 建立專案目錄結構
- **狀態**: 待完成
- **要建立的目錄**:
  ```
  src/main/java/com/fivepapa/backend/
  ├── config/           # 配置類別
  ├── entity/           # JPA 實體
  ├── repository/       # 資料存取層
  ├── service/          # 商業邏輯層
  ├── controller/       # REST 控制器
  ├── dto/              # 資料傳輸物件
  ├── exception/        # 自訂例外
  ├── security/         # 安全設定
  └── util/             # 工具類別
  ```

---

## ⏸️ Phase 4: Entity 與 Repository Layer

### 14. ⏸️ 建立 User Entity
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/entity/User.java`
- **欄位**:
  - `id` (Long, Primary Key, Auto Increment)
  - `username` (String, Unique, Not Null)
  - `password` (String, Not Null, BCrypt encrypted)
  - `email` (String, Unique, Not Null)
  - `role` (String, Default: "USER")
  - `createdAt` (LocalDateTime)
  - `updatedAt` (LocalDateTime)
- **Annotations**:
  - `@Entity`
  - `@Table(name = "users")`
  - `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` (Lombok)

### 15. ⏸️ 建立 UserRepository
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/repository/UserRepository.java`
- **繼承**: `JpaRepository<User, Long>`
- **自訂方法**:
  - `Optional<User> findByUsername(String username)`
  - `Optional<User> findByEmail(String email)`
  - `boolean existsByUsername(String username)`
  - `boolean existsByEmail(String email)`

### 16. ⏸️ Commit: Entity 與 Repository
- **狀態**: 待完成
- **預計 Commit message**: `feat: create User entity and repository`
- **包含檔案**:
  - User.java
  - UserRepository.java

---

## ⏸️ Phase 5: Security 與 JWT 設定

### 17. ⏸️ 建立 JWT Utility 類別
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/util/JwtTokenProvider.java`
- **方法**:
  - `String generateAccessToken(Authentication auth)` - 產生 Access Token (1 hour)
  - `String generateRefreshToken(Authentication auth)` - 產生 Refresh Token (7 days)
  - `boolean validateToken(String token)` - 驗證 Token 有效性
  - `String getUsernameFromToken(String token)` - 從 Token 取得 username

### 18. ⏸️ 建立 CustomUserDetails
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/security/CustomUserDetails.java`
- **實作**: `UserDetails` interface
- **說明**: 包裝 User entity，提供給 Spring Security 使用

### 19. ⏸️ 建立 CustomUserDetailsService
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/security/CustomUserDetailsService.java`
- **實作**: `UserDetailsService` interface
- **方法**: `UserDetails loadUserByUsername(String username)`
- **說明**: 從資料庫載入使用者資料

### 20. ⏸️ 建立 JwtAuthenticationFilter
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/security/JwtAuthenticationFilter.java`
- **繼承**: `OncePerRequestFilter`
- **功能**:
  - 從 Request Header 取出 JWT Token
  - 驗證 Token
  - 設定 SecurityContext

### 21. ⏸️ 建立 SecurityConfig
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/config/SecurityConfig.java`
- **設定項目**:
  - SecurityFilterChain
  - PasswordEncoder (BCrypt, strength = 10)
  - AuthenticationManager
  - **CORS 設定** (允許 `https://elvis1056.github.io`)
  - 路徑權限設定:
    - `/api/v1/auth/**` - 允許匿名存取
    - `/h2-console/**` - 允許存取（開發用）
    - 其他路徑 - 需要認證

### 22. ⏸️ Commit: Security 設定
- **狀態**: 待完成
- **預計 Commit message**: `feat: configure Spring Security with JWT and CORS`
- **包含檔案**:
  - JwtTokenProvider.java
  - CustomUserDetails.java
  - CustomUserDetailsService.java
  - JwtAuthenticationFilter.java
  - SecurityConfig.java

---

## ⏸️ Phase 6: DTO 與 Exception

### 23. ⏸️ 建立 DTO 類別
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/dto/`
- **類別清單**:
  1. `RegisterRequest.java`
     - username (String, @NotBlank, @Size(min=3, max=50))
     - password (String, @NotBlank, @Size(min=8))
     - email (String, @NotBlank, @Email)
  2. `LoginRequest.java`
     - username (String, @NotBlank)
     - password (String, @NotBlank)
  3. `LoginResponse.java`
     - accessToken (String)
     - refreshToken (String)
     - expiresIn (Long)
  4. `UserResponse.java`
     - id (Long)
     - username (String)
     - email (String)
     - role (String)
     - createdAt (LocalDateTime)
  5. `ApiResponse.java`
     - success (Boolean)
     - message (String)
     - data (Object)
     - timestamp (LocalDateTime)

### 24. ⏸️ 建立 Custom Exception
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/exception/`
- **類別清單**:
  1. `UserNotFoundException.java` (extends RuntimeException)
  2. `UsernameAlreadyExistsException.java` (extends RuntimeException)
  3. `EmailAlreadyExistsException.java` (extends RuntimeException)
  4. `InvalidCredentialsException.java` (extends RuntimeException)

### 25. ⏸️ 建立 Global Exception Handler
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/exception/GlobalExceptionHandler.java`
- **Annotation**: `@RestControllerAdvice`
- **處理的 Exception**:
  - `UserNotFoundException` → 404
  - `UsernameAlreadyExistsException` → 409
  - `EmailAlreadyExistsException` → 409
  - `InvalidCredentialsException` → 401
  - `MethodArgumentNotValidException` → 400
  - `Exception` → 500

### 26. ⏸️ Commit: DTO 與 Exception
- **狀態**: 待完成
- **預計 Commit message**: `feat: create DTOs and exception handling`
- **包含檔案**: dto/ 和 exception/ 下所有檔案

---

## ⏸️ Phase 7: Service Layer

### 27. ⏸️ 建立 AuthService
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/service/AuthService.java`
- **方法**:
  1. `User register(RegisterRequest request)`
     - 驗證 username 不重複
     - 驗證 email 不重複
     - 驗證密碼強度
     - 加密密碼（BCrypt）
     - 儲存使用者
  2. `LoginResponse login(LoginRequest request)`
     - 驗證帳號密碼
     - 產生 JWT Token
     - 回傳 LoginResponse
  3. `LoginResponse refreshToken(String refreshToken)`
     - 驗證 Refresh Token
     - 產生新的 Access Token
     - 回傳 LoginResponse

### 28. ⏸️ 建立 UserService
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/service/UserService.java`
- **方法**:
  1. `UserResponse findById(Long id)`
  2. `UserResponse findByUsername(String username)`
  3. `UserResponse updateUser(Long id, UpdateUserRequest request)`
  4. `void deleteUser(Long id)`

### 29. ⏸️ Commit: Service Layer
- **狀態**: 待完成
- **預計 Commit message**: `feat: implement AuthService and UserService`
- **包含檔案**:
  - AuthService.java
  - UserService.java

---

## ⏸️ Phase 8: Controller Layer

### 30. ⏸️ 建立 AuthController
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/controller/AuthController.java`
- **Annotation**: `@RestController`, `@RequestMapping("/api/v1/auth")`
- **Endpoints**:
  1. `POST /api/v1/auth/register` - 註冊新使用者
     - Request: RegisterRequest
     - Response: ApiResponse<UserResponse> (201 Created)
  2. `POST /api/v1/auth/login` - 登入
     - Request: LoginRequest
     - Response: ApiResponse<LoginResponse> (200 OK)
  3. `POST /api/v1/auth/refresh` - 更新 Access Token
     - Request: RefreshTokenRequest
     - Response: ApiResponse<LoginResponse> (200 OK)

### 31. ⏸️ 建立 UserController
- **狀態**: 待完成
- **檔案位置**: `/src/main/java/com/fivepapa/backend/controller/UserController.java`
- **Annotation**: `@RestController`, `@RequestMapping("/api/v1/users")`
- **Endpoints**:
  1. `GET /api/v1/users/{id}` - 取得使用者資料
     - 需要認證
     - Response: ApiResponse<UserResponse> (200 OK)
  2. `PUT /api/v1/users/{id}` - 更新使用者資料
     - 需要認證
     - Request: UpdateUserRequest
     - Response: ApiResponse<UserResponse> (200 OK)
  3. `DELETE /api/v1/users/{id}` - 刪除使用者
     - 需要認證
     - Response: ApiResponse<Void> (204 No Content)

### 32. ⏸️ Commit: Controller Layer
- **狀態**: 待完成
- **預計 Commit message**: `feat: implement REST API controllers`
- **包含檔案**:
  - AuthController.java
  - UserController.java

---

## ⏸️ Phase 9: 單元測試

### 33. ⏸️ 建立 AuthService 單元測試
- **狀態**: 待完成
- **檔案位置**: `/src/test/java/com/fivepapa/backend/service/AuthServiceTest.java`
- **測試案例**:
  1. `shouldRegisterUser_WhenValidRequest()`
  2. `shouldThrowException_WhenUsernameExists()`
  3. `shouldThrowException_WhenEmailExists()`
  4. `shouldLogin_WhenValidCredentials()`
  5. `shouldThrowException_WhenInvalidPassword()`
  6. `shouldThrowException_WhenUserNotFound()`
  7. `shouldRefreshToken_WhenValidRefreshToken()`

### 34. ⏸️ 建立 UserService 單元測試
- **狀態**: 待完成
- **檔案位置**: `/src/test/java/com/fivepapa/backend/service/UserServiceTest.java`
- **測試案例**:
  1. `shouldFindUserById_WhenUserExists()`
  2. `shouldThrowException_WhenUserNotFound()`
  3. `shouldUpdateUser_WhenValidRequest()`
  4. `shouldDeleteUser_WhenUserExists()`

### 35. ⏸️ Commit: 單元測試
- **狀態**: 待完成
- **預計 Commit message**: `test: add unit tests for services`
- **包含檔案**:
  - AuthServiceTest.java
  - UserServiceTest.java

---

## ⏸️ Phase 10: 整合測試

### 36. ⏸️ 建立 AuthController 整合測試
- **狀態**: 待完成
- **檔案位置**: `/src/test/java/com/fivepapa/backend/controller/AuthControllerTest.java`
- **測試案例**:
  1. `shouldRegister_WhenValidRequest()` - 201 Created
  2. `shouldReturn400_WhenInvalidRequest()` - 驗證失敗
  3. `shouldReturn409_WhenUsernameExists()` - Username 重複
  4. `shouldLogin_WhenValidCredentials()` - 200 OK
  5. `shouldReturn401_WhenInvalidCredentials()` - 登入失敗

### 37. ⏸️ 建立 UserController 整合測試
- **狀態**: 待完成
- **檔案位置**: `/src/test/java/com/fivepapa/backend/controller/UserControllerTest.java`
- **測試案例**:
  1. `shouldGetUser_WhenAuthenticated()` - 200 OK
  2. `shouldReturn401_WhenNotAuthenticated()` - 未認證
  3. `shouldReturn404_WhenUserNotFound()` - 使用者不存在
  4. `shouldUpdateUser_WhenAuthenticated()` - 200 OK

### 38. ⏸️ Commit: 整合測試
- **狀態**: 待完成
- **預計 Commit message**: `test: add integration tests for controllers`
- **包含檔案**:
  - AuthControllerTest.java
  - UserControllerTest.java

---

## ⏸️ Phase 11: 驗證與測試

### 39. ⏸️ 執行所有測試
- **狀態**: 待完成
- **指令**: `./mvnw test`
- **驗證項目**:
  - 所有測試通過
  - 測試覆蓋率 > 70%

### 40. ⏸️ 啟動應用程式
- **狀態**: 待完成
- **指令**: `./mvnw spring-boot:run`
- **驗證項目**:
  - 應用程式正常啟動
  - 無錯誤訊息
  - H2 Console 可以存取 (http://localhost:8080/h2-console)
  - Swagger UI 可以存取 (如果有設定)

### 41. ⏸️ Commit: 測試通過
- **狀態**: 待完成
- **預計 Commit message**: `test: all tests passing`

---

## ⏸️ Phase 12: 手動測試與文件

### 42. ⏸️ 建立 Postman 測試指南
- **狀態**: 待完成
- **檔案位置**: `/docs/MANUAL_TESTING_GUIDE.md`
- **內容**:
  - Postman 安裝與設定
  - 環境變數設定
  - 所有 API 的測試步驟
  - 預期的 Request/Response 範例
  - curl 指令範例

### 43. ⏸️ 更新 API_TESTING.md
- **狀態**: 待完成
- **檔案位置**: `/docs/02_API_TESTING.md`
- **更新內容**:
  - 把範例 URL 改成實際可用的
  - 更新 Request/Response 範例
  - 新增實際測試結果

### 44. ⏸️ 手動測試所有 API
- **狀態**: 待完成
- **使用工具**: Postman
- **測試項目**:
  1. 註冊新使用者
  2. 使用相同 username 註冊（應失敗）
  3. 登入
  4. 使用錯誤密碼登入（應失敗）
  5. 取得使用者資料（帶 Token）
  6. 取得使用者資料（不帶 Token，應失敗）
  7. 更新使用者資料
  8. 刪除使用者

### 45. ⏸️ Commit: 文件更新
- **狀態**: 待完成
- **預計 Commit message**: `docs: update API testing guide with working examples`
- **包含檔案**:
  - MANUAL_TESTING_GUIDE.md
  - 02_API_TESTING.md

---

## ⏸️ Phase 13: 完成與合併

### 46. ⏸️ 最終檢查
- **狀態**: 待完成
- **檢查項目**:
  - [ ] 所有程式碼符合 DEV_STANDARDS.md
  - [ ] 所有測試通過
  - [ ] 應用程式可以正常啟動
  - [ ] 所有 API 手動測試通過
  - [ ] 文件已更新
  - [ ] 無 TODO 或 FIXME 註解
  - [ ] 無 console.log 或 System.out.println（除了必要的 logging）

### 47. ⏸️ Merge 到 main
- **狀態**: 待完成
- **指令**:
  ```bash
  git checkout main
  git merge feature/member-system
  git branch -d feature/member-system
  ```

### 48. ⏸️ 建立 Git tag
- **狀態**: 待完成
- **指令**: `git tag -a v0.1.0 -m "Release v0.1.0: Member system"`
- **版本**: v0.1.0
- **說明**: 會員系統第一版完成

---

## 📝 Notes & Issues

### 已解決的問題
1. **Java 版本問題** (2025-10-07)
   - 問題: 系統有 Java 8 和 Java 17，但預設是 Java 8
   - 解決: 使用 SDKMAN 管理，每次執行時設定環境變數
   - 負責人: Claude

2. **SDKMAN 環境變數問題** (2025-10-07)
   - 問題: Claude Code 的 shell 環境沒有載入 ~/.zshrc
   - 解決: 手動設定 JAVA_HOME 和 PATH
   - 負責人: Claude

### 待解決的問題
- 無

### 技術決策記錄
1. **使用 H2 資料庫作為開發環境** (2025-10-07)
   - 原因: 不需要安裝，方便開發和測試
   - 未來會切換到 MySQL

2. **CORS 設定允許 GitHub Pages** (2025-10-07)
   - 允許的 Origin: `https://elvis1056.github.io`
   - 開發時也允許: `http://localhost:3000`

3. **JWT Token 有效期限** (2025-10-07)
   - Access Token: 1 小時
   - Refresh Token: 7 天

---

## 🔄 Change Log

### 2025-10-07
- 建立此進度追蹤文件
- 完成 Phase 1: 環境設定與專案初始化 (8/8 tasks)
- 開始 Phase 2: 依賴設定與基礎配置 (1/4 tasks in progress)

---

## 📚 Related Documents

- [Development Standards](./00_DEV_STANDARDS.md)
- [Database Basics](./01_DATABASE_BASICS.md)
- [API Testing Guide](./02_API_TESTING.md)
- [Unit Testing Guide](./03_UNIT_TESTING.md)
- [Security Basics](./04_SECURITY_BASICS.md)
- [README](../README.md)

---

**Last updated by**: Claude
**Next review date**: 每次完成一個 Phase 後更新
