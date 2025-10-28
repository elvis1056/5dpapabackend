# 🔐 安全性更新文件 - HttpOnly Cookie + CSRF 防護

## 📋 更新摘要

本次更新將認證機制從 **localStorage** 改為 **HttpOnly Cookie + CSRF 防護**，大幅提升安全性。

**更新日期**: 2025-10-27

---

## 🎯 更新內容

### 1. **SecurityConfig.java** - 啟用 CSRF 防護

**修改內容**:
- ✅ 啟用 Cookie-based CSRF token repository
- ✅ Login 和 Register 不需要 CSRF token（首次請求無法取得 token）
- ✅ 其他所有 POST/PUT/DELETE/PATCH 請求需要 CSRF token
- ✅ CORS 設定允許 `X-XSRF-TOKEN` header

**CSRF 配置**:
```java
.csrf(csrf -> csrf
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    .csrfTokenRequestHandler(requestHandler)
    .ignoringRequestMatchers("/api/auth/login", "/api/auth/register")
)
```

**說明**:
- `withHttpOnlyFalse()`: CSRF token cookie 不設定 HttpOnly，讓前端可以讀取（安全的，因為 CSRF token 本身不是機密）
- `ignoringRequestMatchers`: Login 和 Register 豁免 CSRF 檢查

---

### 2. **AuthController.java** - 使用 HttpOnly Cookie

**修改內容**:
- ✅ 登入/註冊時將 refresh token 儲存到 HttpOnly cookie
- ✅ Response body 不再回傳 `refreshToken`（設為 null）
- ✅ Refresh API 從 cookie 讀取 refresh token（不再從 header）
- ✅ 新增 `/api/auth/logout` endpoint 清除 cookie
- ✅ Token rotation - 每次 refresh 都換新 token

**Cookie 設定**:
```java
Cookie cookie = new Cookie("refreshToken", refreshToken);
cookie.setHttpOnly(true);        // 防止 XSS 攻擊
cookie.setSecure(true);          // 僅 HTTPS 傳送
cookie.setPath("/");
cookie.setMaxAge(7 * 24 * 60 * 60);  // 7 天
cookie.setAttribute("SameSite", "None");  // 允許跨域
```

**重要**:
- `HttpOnly=true`: JavaScript 無法讀取，完全防止 XSS 竊取
- `Secure=true`: 僅在 HTTPS 傳送（**本地開發需改為 false**）
- `SameSite=None`: 前後端分離必須設定（需配合 Secure=true）

---

### 3. **LoginResponse.java** - 不回傳 refreshToken

**修改內容**:
- ✅ 加上 `@JsonInclude(JsonInclude.Include.NON_NULL)`
- ✅ `refreshToken` 欄位設為 null 時不序列化

**回傳格式**:
```json
{
  "token": "eyJhbGc...",
  "username": "user123",
  "email": "user@example.com",
  "role": "USER"
  // ❌ 不再有 refreshToken 欄位
}
```

---

### 4. **CsrfController.java** - 提供 CSRF Token

**新增檔案**: `src/main/java/com/fivepapa/backend/common/controller/CsrfController.java`

**API Endpoint**:
```
GET /api/csrf
```

**回傳範例**:
```json
{
  "token": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "headerName": "X-XSRF-TOKEN",
  "parameterName": "_csrf"
}
```

**用途**:
- 前端初次載入時呼叫此 API 取得 CSRF token
- 將 token 值放在 `X-XSRF-TOKEN` header 中

---

### 5. **環境變數新增**

**新增變數**: `.env.example`
```bash
REFRESH_TOKEN_EXPIRATION_DAYS=7
```

**說明**:
- 控制 refresh token cookie 的有效期限（天數）
- 預設 7 天

---

## 🔒 安全性改善對照表

| 項目 | 修改前 | 修改後 |
|------|--------|--------|
| **Refresh Token 儲存** | localStorage（明文） | HttpOnly Cookie（瀏覽器管理） |
| **XSS 防護** | ❌ 可被竊取 | ✅ JavaScript 無法存取 |
| **CSRF 防護** | ❌ 無 | ✅ Token-based CSRF 保護 |
| **Token Rotation** | ❌ 無 | ✅ 每次 refresh 換新 token |
| **自動過期** | 需手動管理 | Cookie 自動過期 |
| **跨分頁共享** | ✅ 共享 | ✅ 共享（Cookie 機制） |
| **使用者體驗** | 永久登入 | 7 天內免登入 |

---

## 🚀 部署步驟

### **1. 更新環境變數**

在 `.env` 檔案中新增（或 Render 環境變數）：
```bash
REFRESH_TOKEN_EXPIRATION_DAYS=7
```

### **2. 本地開發設定**

**重要**: 本地開發時需將 `cookie.setSecure(false)`，因為 localhost 不是 HTTPS。

修改 `AuthController.java` line 125:
```java
// Local development
cookie.setSecure(false);  // Set to true in production

// Or use environment variable
@Value("${COOKIE_SECURE:true}")
private boolean cookieSecure;

cookie.setSecure(cookieSecure);
```

建議新增環境變數：
```bash
# .env (local)
COOKIE_SECURE=false

# Render (production)
COOKIE_SECURE=true
```

### **3. 建置並測試**

```bash
# 建置專案
./mvnw clean package

# 執行本地測試
./mvnw spring-boot:run

# 測試 API endpoints
curl http://localhost:8080/api/csrf
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test123"}' \
  --cookie-jar cookies.txt

curl http://localhost:8080/api/auth/refresh \
  --cookie cookies.txt
```

### **4. 部署到 Render**

```bash
git add .
git commit -m "feat: 實作 HttpOnly Cookie + CSRF 防護機制"
git push origin main
```

Render 會自動部署。

---

## 📚 API 使用說明（給前端）

### **1. 登入流程**

```typescript
// Step 1: 登入
const response = await fetch('https://api.example.com/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  credentials: 'include',  // 重要！讓瀏覽器處理 Cookie
  body: JSON.stringify({ username, password })
});

const data = await response.json();
// data = { token: "...", username: "...", email: "...", role: "..." }
// ❌ 沒有 refreshToken（已存在 HttpOnly cookie）
```

### **2. 需要認證的請求**

```typescript
// Step 2: 取得 CSRF token
const csrfResponse = await fetch('https://api.example.com/api/csrf', {
  credentials: 'include'
});
const csrfData = await csrfResponse.json();
const csrfToken = csrfData.token;

// Step 3: 發送需要認證的請求
const response = await fetch('https://api.example.com/api/users/profile', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${accessToken}`,
    'Content-Type': 'application/json',
    'X-XSRF-TOKEN': csrfToken  // CSRF token
  },
  credentials: 'include',
  body: JSON.stringify({ name: 'New Name' })
});
```

### **3. Token Refresh**

```typescript
// Access token 過期時自動 refresh
const response = await fetch('https://api.example.com/api/auth/refresh', {
  method: 'POST',
  credentials: 'include'  // 瀏覽器自動帶上 refreshToken cookie
});

const data = await response.json();
// 取得新的 access token
// 新的 refresh token 已自動更新到 cookie
```

### **4. 登出**

```typescript
const response = await fetch('https://api.example.com/api/auth/logout', {
  method: 'POST',
  headers: { 'X-XSRF-TOKEN': csrfToken },
  credentials: 'include'
});
// Cookie 已被清除
```

---

## ⚠️ 注意事項

### **1. HTTPS 要求**

**Production 環境必須使用 HTTPS**，否則：
- `Secure=true` 的 cookie 不會傳送
- `SameSite=None` 需要 Secure 屬性

### **2. CORS 設定**

確保前端 domain 在 `SecurityConfig.java` 的 allowedOriginPatterns 中：
```java
configuration.setAllowedOriginPatterns(Arrays.asList(
    "https://yourdomain.com",
    "http://localhost:3000"
));
```

### **3. CSRF Token 管理**

前端需要：
- 首次載入時呼叫 `/api/csrf` 取得 token
- 所有 POST/PUT/DELETE/PATCH 請求帶上 `X-XSRF-TOKEN` header
- Token 存在記憶體中（不要存 localStorage）

### **4. 瀏覽器相容性**

- 所有現代瀏覽器支援 HttpOnly Cookie
- Safari 對 `SameSite=None` 有特殊處理（需 iOS 13+）

---

## 🐛 疑難排解

### **問題 1: Cookie 沒有被設定**

**檢查**:
1. 前端請求是否加上 `credentials: 'include'`
2. 後端 CORS 是否設定 `setAllowCredentials(true)`
3. 是否使用 HTTPS（Production）

### **問題 2: CSRF token 驗證失敗**

**檢查**:
1. 是否正確讀取 `XSRF-TOKEN` cookie
2. Header 名稱是否為 `X-XSRF-TOKEN`（注意大小寫）
3. Login/Register 不需要 CSRF token

### **問題 3: 本地開發 Cookie 不工作**

**解決方案**:
```java
// AuthController.java line 125
cookie.setSecure(false);  // 本地開發設為 false
```

或使用環境變數控制。

---

## 📖 參考資料

- [OWASP CSRF Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html)
- [Spring Security CSRF Documentation](https://docs.spring.io/spring-security/reference/features/exploits/csrf.html)
- [HttpOnly Cookie Best Practices](https://owasp.org/www-community/HttpOnly)

---

**如有問題，請聯繫開發團隊。**
