# API Testing Tutorial (API 測試教學)

**Target Audience**: Complete beginners
**Purpose**: Learn how to test REST APIs using Postman and curl

---

## 1. What is an API? (什麼是 API？)

### 1.1 生活化比喻

**API = 餐廳的服務生**

想像你去餐廳：
1. 你（前端）看菜單，決定要點什麼
2. 你告訴**服務生**（API）：「我要一份牛排，三分熟」
3. 服務生去廚房（後端）傳達你的需求
4. 廚房做好後，服務生把牛排端給你

**API 就是那個服務生**：
- 接收你的請求（點餐）
- 傳遞給後端處理（廚房）
- 回傳結果給你（端菜）

### 1.2 實際範例

**前端（網頁）想要取得使用者資料**：

```
前端: 「給我 ID=1 的使用者資料」
  ↓
API: GET /api/v1/users/1
  ↓
後端: 從資料庫查詢 ID=1 的使用者
  ↓
API: 回傳 {"id": 1, "username": "elvis", "email": "elvis@example.com"}
  ↓
前端: 顯示在網頁上
```

---

## 2. REST API Basics (REST API 基礎)

### 2.1 HTTP Methods (HTTP 方法)

| Method | 用途 | 範例 |
|--------|------|------|
| **GET** | 取得資料（查詢） | 取得使用者清單、取得單一使用者 |
| **POST** | 新增資料（建立） | 註冊新使用者、發表新文章 |
| **PUT** | 更新整筆資料（完整更新） | 更新使用者所有資訊 |
| **PATCH** | 更新部分資料（部分更新） | 只更新使用者的 email |
| **DELETE** | 刪除資料 | 刪除使用者、刪除文章 |

### 2.2 URL Structure (網址結構)

```
http://localhost:8080/api/v1/users/1
│      │         │    │   │  │     │
│      │         │    │   │  │     └─ ID (特定資源)
│      │         │    │   │  └─────── Resource (資源名稱)
│      │         │    │   └────────── Version (API 版本)
│      │         │    └────────────── Base Path
│      │         └─────────────────── Port (埠號)
│      └───────────────────────────── Host (主機)
└──────────────────────────────────── Protocol (協定)
```

### 2.3 Request & Response (請求與回應)

**Request (請求)** 包含：
- **URL**: 要存取的位置
- **Method**: 要做什麼動作 (GET/POST/PUT/DELETE)
- **Headers**: 額外資訊（例如：認證 Token）
- **Body**: 要傳送的資料（只有 POST/PUT/PATCH 才有）

**Response (回應)** 包含：
- **Status Code**: 狀態碼（200 成功、404 找不到、500 伺服器錯誤）
- **Headers**: 回應的額外資訊
- **Body**: 實際的資料（通常是 JSON 格式）

---

## 3. HTTP Status Codes (HTTP 狀態碼)

### 3.1 常見狀態碼

| Code | 意義 | 說明 |
|------|------|------|
| **200** | OK | 成功 |
| **201** | Created | 建立成功（通常用於 POST） |
| **204** | No Content | 成功但沒有回傳內容 |
| **400** | Bad Request | 請求格式錯誤 |
| **401** | Unauthorized | 未登入或 Token 無效 |
| **403** | Forbidden | 沒有權限 |
| **404** | Not Found | 找不到資源 |
| **409** | Conflict | 資料衝突（例如：username 已存在） |
| **500** | Internal Server Error | 伺服器錯誤 |

### 3.2 狀態碼分類

- **2xx**: 成功
- **3xx**: 重新導向
- **4xx**: 客戶端錯誤（你的請求有問題）
- **5xx**: 伺服器錯誤（後端有 bug）

---

## 4. Postman 安裝與使用

### 4.1 安裝 Postman

1. 前往 https://www.postman.com/downloads/
2. 下載對應你作業系統的版本
3. 安裝並開啟 Postman

**或使用網頁版**：
- 前往 https://web.postman.co/
- 用 Google 帳號登入即可使用

### 4.2 Postman 介面說明

```
┌─────────────────────────────────────────────────────┐
│ [Collections] [History] [Environments]              │
├─────────────────────────────────────────────────────┤
│ GET ▼  http://localhost:8080/api/v1/users   [Send] │ ← Request Bar
├─────────────────────────────────────────────────────┤
│ Params | Authorization | Headers | Body | ...      │ ← Request Tabs
├─────────────────────────────────────────────────────┤
│ (Request details here)                              │
├─────────────────────────────────────────────────────┤
│ Body | Cookies | Headers | Test Results | ...      │ ← Response Tabs
├─────────────────────────────────────────────────────┤
│ Status: 200 OK   Time: 125ms   Size: 1.2KB         │ ← Response Info
│                                                     │
│ {                                                   │
│   "success": true,                                  │ ← Response Body
│   "data": [...]                                     │
│ }                                                   │
└─────────────────────────────────────────────────────┘
```

---

## 5. 實際測試範例

### 5.1 範例 1: 註冊新使用者 (POST)

**API Endpoint**: `POST http://localhost:8080/api/v1/auth/register`

**在 Postman 中設定**:
1. Method: 選擇 `POST`
2. URL: 輸入 `http://localhost:8080/api/v1/auth/register`
3. 點擊 `Body` tab
4. 選擇 `raw` 和 `JSON`
5. 輸入以下內容：

```json
{
  "username": "elvis",
  "password": "MyPassword123!",
  "email": "elvis@example.com",
  "age": 25
}
```

6. 點擊 `Send`

**預期回應** (Status: 201 Created):
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "username": "elvis",
    "email": "elvis@example.com",
    "createdAt": "2025-10-07T12:00:00Z"
  }
}
```

### 5.2 範例 2: 登入取得 Token (POST)

**API Endpoint**: `POST http://localhost:8080/api/v1/auth/login`

**Request Body**:
```json
{
  "username": "elvis",
  "password": "MyPassword123!"
}
```

**預期回應** (Status: 200 OK):
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...",
    "expiresIn": 3600
  }
}
```

**重要**：把 `accessToken` 複製起來，下一步會用到！

### 5.3 範例 3: 取得使用者資料 (GET with Authentication)

**API Endpoint**: `GET http://localhost:8080/api/v1/users/1`

**在 Postman 中設定**:
1. Method: 選擇 `GET`
2. URL: 輸入 `http://localhost:8080/api/v1/users/1`
3. 點擊 `Authorization` tab
4. Type: 選擇 `Bearer Token`
5. Token: 貼上剛才的 `accessToken`
6. 點擊 `Send`

**預期回應** (Status: 200 OK):
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "elvis",
    "email": "elvis@example.com",
    "age": 25,
    "createdAt": "2025-10-07T12:00:00Z"
  }
}
```

### 5.4 範例 4: 更新使用者資料 (PUT)

**API Endpoint**: `PUT http://localhost:8080/api/v1/users/1`

**在 Postman 中設定**:
1. Method: 選擇 `PUT`
2. URL: 輸入 `http://localhost:8080/api/v1/users/1`
3. Authorization: Bearer Token（同上）
4. Body: 選擇 `raw` 和 `JSON`

```json
{
  "email": "newemail@example.com",
  "age": 26
}
```

**預期回應** (Status: 200 OK):
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 1,
    "username": "elvis",
    "email": "newemail@example.com",
    "age": 26
  }
}
```

### 5.5 範例 5: 刪除使用者 (DELETE)

**API Endpoint**: `DELETE http://localhost:8080/api/v1/users/1`

**在 Postman 中設定**:
1. Method: 選擇 `DELETE`
2. URL: 輸入 `http://localhost:8080/api/v1/users/1`
3. Authorization: Bearer Token

**預期回應** (Status: 204 No Content):
```json
{
  "success": true,
  "message": "User deleted successfully"
}
```

---

## 6. Using curl (命令列測試)

### 6.1 什麼是 curl？

**curl = 命令列工具，可以發送 HTTP 請求**

好處：
- 不用安裝 Postman
- 可以寫在腳本裡自動化測試
- 可以快速測試

### 6.2 curl 基本語法

```bash
curl [options] [URL]
```

常用 options:
- `-X METHOD`: 指定 HTTP 方法（GET/POST/PUT/DELETE）
- `-H "Header: Value"`: 加入 Header
- `-d "data"`: 傳送資料（Body）
- `-i`: 顯示 Response Headers
- `-v`: 顯示詳細資訊（除錯用）

### 6.3 curl 實際範例

**範例 1: 註冊使用者 (POST)**
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "elvis",
    "password": "MyPassword123!",
    "email": "elvis@example.com",
    "age": 25
  }'
```

**範例 2: 登入**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "elvis",
    "password": "MyPassword123!"
  }'
```

**範例 3: 取得使用者資料（需要 Token）**
```bash
curl -X GET http://localhost:8080/api/v1/users/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

**範例 4: 更新使用者資料**
```bash
curl -X PUT http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d '{
    "email": "newemail@example.com",
    "age": 26
  }'
```

**範例 5: 刪除使用者**
```bash
curl -X DELETE http://localhost:8080/api/v1/users/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

---

## 7. Postman Collections (Postman 集合)

### 7.1 什麼是 Collection?

**Collection = 一組相關的 API 請求**

好處：
- 把相關的 API 整理在一起
- 可以匯出分享給其他人
- 可以一鍵執行所有測試

### 7.2 建立 Collection

1. 點擊左側 `Collections`
2. 點擊 `+` 或 `Create Collection`
3. 命名：`5dpapa Backend API`
4. 建立資料夾：
   - `Auth` (認證相關)
   - `Users` (使用者相關)
   - `Posts` (文章相關，未來用)
   - `Orders` (訂單相關,未來用)

### 7.3 在 Collection 中新增 Request

1. 在 `Auth` 資料夾上按右鍵
2. 選擇 `Add Request`
3. 命名: `Register`
4. 設定 Method、URL、Body
5. 點擊 `Save`

重複步驟建立：
- `Auth/Register`
- `Auth/Login`
- `Users/Get User by ID`
- `Users/Update User`
- `Users/Delete User`

### 7.4 使用 Environment Variables (環境變數)

**為什麼需要？**
- 開發環境用 `http://localhost:8080`
- 正式環境用 `https://api.5dpapa.com`
- 不用每次手動改 URL

**設定步驟**：
1. 點擊右上角 `Environments`
2. 點擊 `+` 建立新環境
3. 命名: `Local Development`
4. 新增變數：
   - `baseUrl`: `http://localhost:8080`
   - `accessToken`: (留空，登入後會自動填入)

5. 在 Request 中使用：
   - URL: `{{baseUrl}}/api/v1/users/1`
   - Authorization: `Bearer {{accessToken}}`

---

## 8. Common Issues (常見問題)

### 8.1 錯誤: Connection refused

**原因**: Spring Boot 應用程式沒有啟動

**解決**:
```bash
mvn spring-boot:run
```

### 8.2 錯誤: 401 Unauthorized

**原因**: Token 無效或過期

**解決**:
1. 重新登入取得新的 Token
2. 檢查 Authorization Header 是否正確設定

### 8.3 錯誤: 400 Bad Request

**原因**: Request Body 格式錯誤

**檢查**:
- JSON 格式是否正確（逗號、引號）
- Content-Type 是否設定為 `application/json`
- 必填欄位是否都有填

### 8.4 錯誤: 500 Internal Server Error

**原因**: 後端程式有 bug

**解決**:
1. 查看 Spring Boot 的 Console 輸出
2. 看錯誤訊息（Exception）
3. 找出哪一行程式碼出問題

---

## 9. Testing Checklist (測試檢查清單)

每次測試 API 時，確認以下項目：

### 9.1 成功情境 (Happy Path)
- [ ] 正確的請求格式
- [ ] 回應狀態碼是否正確（200/201）
- [ ] 回應資料格式是否正確
- [ ] 回應資料內容是否符合預期

### 9.2 錯誤情境 (Error Cases)
- [ ] 必填欄位缺少時，是否回傳 400
- [ ] 未登入時，是否回傳 401
- [ ] 資源不存在時，是否回傳 404
- [ ] 重複資料時，是否回傳 409
- [ ] 錯誤訊息是否清楚

### 9.3 安全性測試
- [ ] 沒有 Token 能否存取需要認證的 API
- [ ] 使用別人的 Token 能否存取自己的資料
- [ ] 密碼是否有加密（不應該在回應中看到明文密碼）

---

## 10. Next Steps (下一步)

看完這份文件後，你應該要會：
- [x] 理解 API 是什麼
- [x] 知道 HTTP Methods 的用途
- [x] 會使用 Postman 測試 API
- [x] 會使用 curl 測試 API
- [x] 知道如何建立 Postman Collection

**實際練習**：
1. 安裝 Postman
2. 等我建立好會員系統 API 後
3. 跟著這份文件測試每一個 API
4. 嘗試故意輸入錯誤資料，看看會發生什麼

**接下來**：
- 看 `UNIT_TESTING.md` 學習如何寫測試
- 看 `SECURITY_BASICS.md` 學習安全性概念
