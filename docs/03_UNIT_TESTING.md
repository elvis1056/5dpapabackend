# Unit Testing Tutorial (單元測試教學)

**Target Audience**: Complete beginners
**Purpose**: Understand why and how to write unit tests

---

## 1. What is Unit Testing? (什麼是單元測試？)

### 1.1 生活化比喻

**單元測試 = 出廠前的品質檢測**

想像你在組裝一台電腦：
- ❌ **不測試**: 全部組好後才開機，壞了也不知道哪個零件有問題
- ✅ **單元測試**: 每裝一個零件都測試一次（CPU 能用嗎？記憶體能用嗎？）

**好處**：
- 及早發現問題
- 知道問題出在哪個零件
- 換零件時確保不會影響其他部分

### 1.2 程式碼範例

**沒有測試的程式碼**：
```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

你怎麼知道這個方法是對的？
- ❌ 寫完就相信它是對的（危險！）
- ❌ 啟動整個應用程式手動測試（太慢！）
- ✅ 寫單元測試（快速、可重複執行）

**有測試的程式碼**：
```java
@Test
void shouldAddTwoNumbers() {
    // Given (準備測試資料)
    Calculator calculator = new Calculator();

    // When (執行要測試的方法)
    int result = calculator.add(2, 3);

    // Then (驗證結果)
    assertEquals(5, result);
}
```

執行測試：
```
✅ shouldAddTwoNumbers - PASSED (5ms)
```

---

## 2. Why Unit Testing? (為什麼要寫單元測試？)

### 2.1 好處

| 好處 | 說明 |
|------|------|
| **及早發現 bug** | 寫完馬上測試，不用等到上線才發現 |
| **重構時有信心** | 改程式碼後跑測試，確保沒壞掉 |
| **文件化** | 測試就是最好的使用範例 |
| **減少手動測試** | 自動化測試比手動快 100 倍 |
| **提升程式碼品質** | 寫測試時會思考設計是否合理 |

### 2.2 實際案例

**情境**：你修改了登入邏輯

**沒有測試**：
1. 手動啟動應用程式
2. 開 Postman 測試登入
3. 測試成功登入
4. 測試密碼錯誤
5. 測試 username 不存在
6. 測試空白輸入
7. ...（每次都要重複這些步驟，超煩）

**有測試**：
1. 修改程式碼
2. 執行 `mvn test`（2 秒內跑完所有測試）
3. 全部通過 ✅（放心，沒壞掉）

---

## 3. Unit Test Structure (測試結構)

### 3.1 AAA Pattern (Given-When-Then)

所有單元測試都遵循這個模式：

```java
@Test
void testName() {
    // Given (Arrange) - 準備測試資料和環境
    // 建立測試所需的物件、變數、假資料

    // When (Act) - 執行要測試的動作
    // 呼叫要測試的方法

    // Then (Assert) - 驗證結果
    // 檢查結果是否符合預期
}
```

### 3.2 實際範例

```java
@Test
void shouldFindUserByUsername_WhenUserExists() {
    // Given: 準備假的資料庫回應
    String username = "elvis";
    User mockUser = User.builder()
        .id(1L)
        .username(username)
        .email("elvis@example.com")
        .build();
    when(userRepository.findByUsername(username))
        .thenReturn(Optional.of(mockUser));

    // When: 執行要測試的方法
    User result = userService.findByUsername(username);

    // Then: 驗證結果
    assertNotNull(result);
    assertEquals(username, result.getUsername());
    assertEquals("elvis@example.com", result.getEmail());
}
```

---

## 4. JUnit 5 Basics (JUnit 5 基礎)

### 4.1 常用 Annotations

| Annotation | 用途 |
|-----------|------|
| `@Test` | 標記這是一個測試方法 |
| `@BeforeEach` | 每個測試方法執行前都會執行 |
| `@AfterEach` | 每個測試方法執行後都會執行 |
| `@BeforeAll` | 所有測試方法執行前執行一次（必須是 static） |
| `@AfterAll` | 所有測試方法執行後執行一次（必須是 static） |
| `@Disabled` | 暫時停用這個測試 |
| `@DisplayName` | 自訂測試名稱（中文也可以） |

### 4.2 範例

```java
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 每個測試執行前都會執行（初始化測試環境）
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("應該成功找到使用者當使用者存在時")
    void shouldFindUserByUsername_WhenUserExists() {
        // 測試內容...
    }

    @Test
    @Disabled("暫時停用，等待 API 完成")
    void shouldThrowException_WhenUserNotFound() {
        // 測試內容...
    }

    @AfterEach
    void tearDown() {
        // 每個測試執行後都會執行（清理資源）
    }
}
```

---

## 5. Assertions (斷言)

### 5.1 常用 Assertions

| Assertion | 用途 | 範例 |
|----------|------|------|
| `assertEquals(expected, actual)` | 檢查兩個值是否相等 | `assertEquals(5, result)` |
| `assertNotEquals(unexpected, actual)` | 檢查兩個值是否不相等 | `assertNotEquals(0, result)` |
| `assertTrue(condition)` | 檢查條件是否為 true | `assertTrue(user.isActive())` |
| `assertFalse(condition)` | 檢查條件是否為 false | `assertFalse(user.isDeleted())` |
| `assertNull(object)` | 檢查物件是否為 null | `assertNull(result)` |
| `assertNotNull(object)` | 檢查物件是否不為 null | `assertNotNull(user)` |
| `assertThrows(Exception.class, ...)` | 檢查是否拋出例外 | `assertThrows(UserNotFoundException.class, () -> ...)` |

### 5.2 實際範例

```java
@Test
void testAssertions() {
    User user = new User(1L, "elvis", "encrypted_pass", "elvis@example.com");

    // 檢查值相等
    assertEquals("elvis", user.getUsername());

    // 檢查值不等
    assertNotEquals("plaintext", user.getPassword());

    // 檢查物件不是 null
    assertNotNull(user);

    // 檢查條件為 true
    assertTrue(user.getEmail().contains("@"));

    // 檢查條件為 false
    assertFalse(user.getUsername().isEmpty());
}

@Test
void shouldThrowException_WhenUserNotFound() {
    // 檢查是否拋出特定例外
    assertThrows(UserNotFoundException.class, () -> {
        userService.findByUsername("nonexistent");
    });
}
```

---

## 6. Mockito Basics (Mockito 基礎)

### 6.1 什麼是 Mock?

**Mock = 假的物件**

**為什麼需要？**
- 測試 `UserService` 時，不想真的連接資料庫
- 測試 `OrderService` 時，不想真的扣款
- 測試 `EmailService` 時，不想真的寄信

**Mock 讓你可以**：
- 模擬資料庫回應
- 模擬外部 API 回應
- 控制測試環境

### 6.2 建立 Mock

```java
// 方法 1: 使用 @Mock annotation
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks  // 自動注入 mock 物件到 UserService
    private UserService userService;
}

// 方法 2: 手動建立 mock
public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }
}
```

### 6.3 設定 Mock 行為

```java
@Test
void example() {
    // 當呼叫 findById(1L) 時，回傳假的 User
    when(userRepository.findById(1L))
        .thenReturn(Optional.of(mockUser));

    // 當呼叫 findById(999L) 時，回傳空的 Optional
    when(userRepository.findById(999L))
        .thenReturn(Optional.empty());

    // 當呼叫 save() 時，拋出例外
    when(userRepository.save(any(User.class)))
        .thenThrow(new DatabaseException("Database error"));
}
```

### 6.4 驗證 Mock 行為

```java
@Test
void shouldSaveUser() {
    // Given
    User user = new User("elvis", "pass123");

    // When
    userService.register(user);

    // Then: 驗證 save() 方法有被呼叫一次
    verify(userRepository, times(1)).save(user);

    // 或者驗證從未被呼叫
    verify(userRepository, never()).delete(any(User.class));
}
```

---

## 7. Real-World Examples (實際範例)

### 7.1 測試 Service Layer

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should register user successfully with encrypted password")
    void shouldRegisterUser_WithEncryptedPassword() {
        // Given
        String rawPassword = "MyPassword123!";
        String encryptedPassword = "$2a$10$encrypted...";

        User inputUser = User.builder()
            .username("elvis")
            .password(rawPassword)
            .email("elvis@example.com")
            .build();

        User savedUser = User.builder()
            .id(1L)
            .username("elvis")
            .password(encryptedPassword)
            .email("elvis@example.com")
            .build();

        when(passwordEncoder.encode(rawPassword)).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.register(inputUser);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("elvis", result.getUsername());
        assertEquals(encryptedPassword, result.getPassword());

        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowException_WhenUsernameExists() {
        // Given
        String username = "elvis";
        User user = User.builder()
            .username(username)
            .password("pass123")
            .build();

        when(userRepository.existsByUsername(username)).thenReturn(true);

        // When & Then
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.register(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should find user by username when user exists")
    void shouldFindUserByUsername_WhenUserExists() {
        // Given
        String username = "elvis";
        User mockUser = User.builder()
            .id(1L)
            .username(username)
            .email("elvis@example.com")
            .build();

        when(userRepository.findByUsername(username))
            .thenReturn(Optional.of(mockUser));

        // When
        User result = userService.findByUsername(username);

        // Then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowException_WhenUserNotFound() {
        // Given
        String username = "nonexistent";
        when(userRepository.findByUsername(username))
            .thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.findByUsername(username);
        });
    }
}
```

### 7.2 測試 Controller Layer (Integration Test)

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // Spring Boot 專用，會替換掉 Spring Context 中的 bean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;  // 用來轉換 JSON

    @Test
    @DisplayName("Should register user and return 201 Created")
    void shouldRegisterUser() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest("elvis", "Pass123!", "elvis@example.com");
        User mockUser = User.builder()
            .id(1L)
            .username("elvis")
            .email("elvis@example.com")
            .build();

        when(userService.register(any(User.class))).thenReturn(mockUser);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.username").value("elvis"));
    }

    @Test
    @DisplayName("Should return 400 when username is empty")
    void shouldReturn400_WhenUsernameIsEmpty() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest("", "Pass123!", "elvis@example.com");

        // When & Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 401 when access without token")
    void shouldReturn401_WhenNoToken() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/users/1"))
            .andExpect(status().isUnauthorized());
    }
}
```

---

## 8. Test Coverage (測試覆蓋率)

### 8.1 什麼是測試覆蓋率？

**測試覆蓋率 = 你的測試執行了多少程式碼**

```java
public int divide(int a, int b) {
    if (b == 0) {                    // Line 1
        throw new ArithmeticException(); // Line 2
    }
    return a / b;                    // Line 3
}
```

**測試 1**（只測試正常情況）：
```java
@Test
void shouldDivide() {
    assertEquals(2, divide(4, 2));
}
```
覆蓋率：66%（只執行了 Line 1 和 Line 3）

**測試 2**（加上錯誤情況）：
```java
@Test
void shouldThrowException_WhenDivideByZero() {
    assertThrows(ArithmeticException.class, () -> divide(4, 0));
}
```
覆蓋率：100%（所有程式碼都被測試到）

### 8.2 推薦的覆蓋率目標

| Layer | 建議覆蓋率 |
|-------|----------|
| Service Layer | 80% 以上 |
| Controller Layer | 70% 以上 |
| Entity/DTO | 不強制（通常是 Lombok 自動產生的 getter/setter） |
| Utility Classes | 90% 以上 |

### 8.3 查看覆蓋率報告

使用 JaCoCo plugin：

```bash
mvn test jacoco:report
```

報告會產生在：`target/site/jacoco/index.html`

---

## 9. Best Practices (最佳實踐)

### 9.1 測試命名規範

**推薦格式**：
```
should[ExpectedBehavior]_When[Condition]
```

**範例**：
```java
shouldReturnUser_WhenUserExists()
shouldThrowException_WhenUserNotFound()
shouldReturn400_WhenPasswordIsTooShort()
```

### 9.2 每個測試只測一件事

❌ **不好的測試**（一次測太多）：
```java
@Test
void testUserService() {
    User user1 = userService.register(new User("elvis", "pass123"));
    assertNotNull(user1);

    User user2 = userService.findById(1L);
    assertEquals("elvis", user2.getUsername());

    userService.delete(1L);
    assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
}
```

✅ **好的測試**（分開測試）：
```java
@Test
void shouldRegisterUser() { ... }

@Test
void shouldFindUserById() { ... }

@Test
void shouldDeleteUser() { ... }
```

### 9.3 測試應該獨立

每個測試不應該依賴其他測試的結果。

❌ **不好的測試**（Test 2 依賴 Test 1）：
```java
private User savedUser;

@Test
void test1_shouldSaveUser() {
    savedUser = userService.save(new User("elvis", "pass123"));
}

@Test
void test2_shouldFindUser() {
    User user = userService.findById(savedUser.getId());  // 依賴 test1
}
```

✅ **好的測試**（各自獨立）：
```java
@Test
void shouldSaveUser() {
    User user = userService.save(new User("elvis", "pass123"));
    assertNotNull(user.getId());
}

@Test
void shouldFindUser() {
    User mockUser = new User(1L, "elvis", "pass123");
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

    User user = userService.findById(1L);
    assertEquals("elvis", user.getUsername());
}
```

### 9.4 使用有意義的測試資料

❌ **不好**：
```java
User user = new User("abc", "123");
```

✅ **好**：
```java
User user = new User("elvis", "MySecurePassword123!");
```

---

## 10. 回答你的疑問：規模越大就越難維護？

### 10.1 你的擔心是對的

**是的，沒有好好規劃的測試會變成負擔**：
- 改一個功能，要改 50 個測試
- 測試跑很慢，每次要等 10 分鐘
- 測試太多重複的程式碼

### 10.2 如何避免？

| 問題 | 解決方法 |
|------|---------|
| 測試太慢 | 只測 Service Layer，不要每個都測 Controller |
| 重複程式碼 | 把共用的測試資料抽出來（Test Fixture） |
| 改一個功能要改很多測試 | 代表你的設計有問題，應該重構 |
| 測試太複雜 | 測試應該簡單，如果測試很複雜代表程式碼設計不好 |

### 10.3 測試金字塔

```
        /\
       /  \  E2E Tests (少量，測試整個流程)
      /    \
     /------\ Integration Tests (中量，測試 API)
    /        \
   /----------\ Unit Tests (大量，測試 Service)
  /______________\
```

**推薦比例**：
- 70% Unit Tests（快速、穩定）
- 20% Integration Tests（測試整合）
- 10% E2E Tests（測試完整流程）

### 10.4 我在這個專案會怎麼做

1. **重點測試 Service Layer**（因為商業邏輯都在這）
2. **Controller 只測試重要的 API**（不是每個都測）
3. **Entity/DTO 不測試**（都是 Lombok 產生的）
4. **建立共用的 Test Fixture**（避免重複程式碼）

---

## 11. Next Steps (下一步)

看完這份文件後，你應該要理解：
- [x] 為什麼要寫單元測試
- [x] 如何用 JUnit 5 寫測試
- [x] 如何用 Mockito 模擬物件
- [x] 測試覆蓋率的意義
- [x] 單元測試的最佳實踐

**接下來**：
- 看 `SECURITY_BASICS.md` 學習安全性概念
- 等我寫好會員系統後，看實際的測試程式碼
- 嘗試自己修改測試，體驗測試的價值

**記住**：
- 測試不是負擔，是保護你的程式碼不會壞掉
- 一開始會覺得麻煩，習慣後會發現超有用
- 改程式碼時，測試會給你信心

如果有任何不懂的地方，隨時問我！
