# 學習筆記 (Learning Notes)

**建立日期**: 2025-10-09
**目的**: 記錄開發過程中學到的重要概念和常見疑問

---

## 📑 目錄

### 一、基礎概念
1. [Field、Constructor、Method 是什麼？](#1-fieldconstructormethod-是什麼)
2. [Package by Feature vs Package by Layer](#2-package-by-feature-vs-package-by-layer)
3. [繼承（Inheritance）和 @Override](#3-繼承inheritance和-override)

### 二、Spring Framework
4. [Spring Security 是什麼？為什麼需要它？](#4-spring-security-是什麼為什麼需要它)
5. [@Value 註解的作用](#5-value-註解的作用)
6. [@Component、@Service、@Repository 的差異](#6-componentservicerepository-的差異)
7. [依賴注入（Dependency Injection）](#7-依賴注入dependency-injection)

### 三、JWT 認證
8. [JWT Token 是什麼？](#8-jwt-token-是什麼)
9. [JWT_SECRET 密鑰是給誰用的？](#9-jwt_secret-密鑰是給誰用的)
10. [Session Stateful vs Stateless](#10-session-stateful-vs-stateless)

### 四、開發工具和文件
11. [如何閱讀 Spring 官方文件（JavaDoc）](#11-如何閱讀-spring-官方文件javadoc)
12. [怎麼知道要用哪個 Spring 類別？](#12-怎麼知道要用哪個-spring-類別)
13. [Maven 是什麼？pom.xml 做什麼用？](#13-maven-是什麼pomxml-做什麼用)

### 五、資料庫相關
14. [JPA Entity 和 Repository 的關係](#14-jpa-entity-和-repository-的關係)
15. [Spring Data JPA 方法命名規則](#15-spring-data-jpa-方法命名規則)

### 六、實務問題
16. [為什麼啟動失敗？找不到環境變數](#16-為什麼啟動失敗找不到環境變數)
17. [CORS 是什麼？為什麼需要設定？](#17-cors-是什麼為什麼需要設定)

---

## 一、基礎概念

### 1. Field、Constructor、Method 是什麼？

#### Field（欄位/屬性）
**定義**：類別中的變數，用來儲存物件的狀態。

**範例**：
```java
public class User {
    private Long id;           // Field
    private String username;   // Field
    private String email;      // Field
}
```

**比喻**：就像一個人的「特徵」
- 姓名、年齡、身高 → 這些都是 Field

---

#### Constructor（建構子）
**定義**：建立物件時呼叫的特殊方法，用來初始化物件。

**範例**：
```java
public class User {
    private String username;

    // Constructor（建構子）
    public User(String username) {
        this.username = username;
    }
}

// 使用
User user = new User("elvis");  // 呼叫建構子
```

**特性**：
- 名稱必須和類別名稱相同
- 沒有回傳值（連 void 都不用寫）
- 可以有多個建構子（多載 Overloading）

**Lombok 簡化**：
```java
@NoArgsConstructor   // 自動產生無參數建構子
@AllArgsConstructor  // 自動產生全參數建構子
public class User {
    private String username;
}
```

---

#### Method（方法）
**定義**：類別中的函式，定義物件的行為。

**範例**：
```java
public class User {
    private String username;

    // Method（方法）
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
```

**比喻**：就像一個人的「動作」
- 走路、說話、吃飯 → 這些都是 Method

---

### 2. Package by Feature vs Package by Layer

#### Package by Layer（按技術層分包）
**結構**：
```
com.fivepapa.backend
├── entity/          # 所有實體
│   ├── User.java
│   ├── Post.java
│   └── Order.java
├── repository/      # 所有 Repository
│   ├── UserRepository.java
│   ├── PostRepository.java
│   └── OrderRepository.java
├── service/         # 所有 Service
└── controller/      # 所有 Controller
```

**優點**：結構簡單，容易理解
**缺點**：功能耦合，難以拆分成微服務

**適用**：小型專案、單體應用

---

#### Package by Feature（按功能分包）⭐ 官方推薦
**結構**：
```
com.fivepapa.backend
├── member/          # 會員功能
│   ├── entity/
│   │   └── User.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── service/
│   └── controller/
├── blog/            # 部落格功能
│   ├── entity/
│   ├── repository/
│   ├── service/
│   └── controller/
└── order/           # 訂單功能
```

**優點**：高內聚低耦合，容易拆分成微服務
**缺點**：初學者可能不習慣

**適用**：中大型專案、微服務架構

**Spring Boot 官方推薦**：Package by Feature
**參考文件**：https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html

---

### 3. 繼承（Inheritance）和 @Override

#### 繼承是什麼？
**定義**：子類別可以「繼承」父類別的方法和屬性。

**語法**：
```java
public class Child extends Parent {
    // 子類別繼承父類別
}
```

**範例**：
```java
// 父類別
public class Animal {
    public void eat() {
        System.out.println("動物在吃東西");
    }
}

// 子類別
public class Dog extends Animal {
    // Dog 自動擁有 eat() 方法
}

// 使用
Dog dog = new Dog();
dog.eat();  // 輸出：動物在吃東西
```

---

#### @Override 是什麼？
**定義**：覆寫（重新定義）父類別的方法。

**範例**：
```java
public class Dog extends Animal {
    @Override
    public void eat() {
        System.out.println("狗在吃骨頭");  // 覆寫父類別的方法
    }
}

Dog dog = new Dog();
dog.eat();  // 輸出：狗在吃骨頭
```

**作用**：
1. ✅ 告訴編譯器：「我要覆寫父類別的方法」
2. ✅ 如果父類別沒有這個方法 → 編譯錯誤（防止拼錯字）

---

#### 抽象方法（Abstract Method）
**定義**：父類別只定義方法簽名，沒有實作，子類別「必須」實作。

**範例**：
```java
// 父類別
public abstract class OncePerRequestFilter {
    // 抽象方法：沒有 { }，沒有實作
    protected abstract void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException;
}

// 子類別（必須實作）
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(...) {
        // 我的實作
    }
}
```

**為什麼要這樣設計？**
- Spring 負責複雜的部分（保證只執行一次）
- 你只需要寫業務邏輯（驗證 JWT）

---

## 二、Spring Framework

### 4. Spring Security 是什麼？為什麼需要它？

#### 定義
**Spring Security** 是 Spring Boot 的安全框架，負責「身份驗證」和「授權」。

#### 比喻：夜店門口的保全

| 角色 | Spring Security 對應 |
|------|---------------------|
| 夜店 | 你的 API |
| 保全 | Spring Security |
| 會員卡 | JWT Token |
| 客人 | 使用者 |

---

#### 沒有 Spring Security 的問題

```java
@GetMapping("/api/users")
public List<User> getAllUsers() {
    return userService.getAllUsers();
}
```

**問題**：
- ❌ 任何人都可以訪問
- ❌ 不需要登入
- ❌ 駭客可以直接拿到所有使用者資料

---

#### 有 Spring Security 的保護

```java
@GetMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")  // 只有 ADMIN 可以
public List<User> getAllUsers() {
    return userService.getAllUsers();
}
```

**Spring Security 會自動**：
1. 攔截請求
2. 檢查是否有 JWT Token
3. 驗證 Token 是否有效
4. 檢查使用者角色
5. 允許/拒絕訪問

---

#### Spring Security 6 vs 5 的差異

| 功能 | Spring Security 5 | Spring Security 6 |
|------|------------------|------------------|
| 配置方式 | 繼承 `WebSecurityConfigurerAdapter` | 建立 `SecurityFilterChain` Bean |
| 語法 | `.and()` 鏈式呼叫 | Lambda DSL |
| 可讀性 | 較差 | 較好 |

**舊版（5.x）**：
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.cors().and()
        .csrf().disable().and()
        .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated();
}
```

**新版（6.x）**：
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
        );
    return http.build();
}
```

---

### 5. @Value 註解的作用

#### 定義
`@Value` 用來從外部讀取設定值，注入到類別的欄位中。

#### 為什麼需要？

**❌ 不好的做法**：設定寫死在程式碼
```java
public class JwtUtil {
    private String secret = "my-secret-key";  // 寫死
}
```

**問題**：
- ❌ 開發/測試/正式環境用同一個密鑰
- ❌ 密鑰會被提交到 Git（不安全）
- ❌ 改密鑰要重新編譯程式碼

**✅ 好的做法**：用 @Value 從外部讀取
```java
public class JwtUtil {
    @Value("${JWT_SECRET}")
    private String secret;  // 從環境變數讀取
}
```

**好處**：
- ✅ 每個環境用不同密鑰
- ✅ 密鑰不會提交到 Git
- ✅ 改密鑰不用重新編譯

---

#### Spring Boot 讀取順序

Spring Boot 會依序從這些地方找設定值：

1. **環境變數**
   ```bash
   export JWT_SECRET="my-secret"
   ```

2. **application.properties**
   ```properties
   JWT_SECRET=my-secret
   ```

3. **命令列參數**
   ```bash
   java -jar app.jar --JWT_SECRET=my-secret
   ```

4. **找不到 → 啟動失敗**
   ```
   Could not resolve placeholder 'JWT_SECRET'
   ```

---

### 6. @Component、@Service、@Repository 的差異

這三個註解都是告訴 Spring：「這是一個元件，請管理它」，但有語意上的區別。

| 註解 | 用途 | 層級 | 範例 |
|------|------|------|------|
| `@Component` | 通用元件 | - | JwtUtil、過濾器 |
| `@Service` | 業務邏輯層 | Service | AuthService、UserService |
| `@Repository` | 資料存取層 | Repository | UserRepository |

**範例**：
```java
@Component
public class JwtUtil { }  // 工具類別

@Service
public class AuthService { }  // 業務邏輯

@Repository
public interface UserRepository extends JpaRepository<User, Long> { }  // 資料存取
```

**為什麼要分？**
- ✅ 程式碼更清楚（一看就知道是什麼層級）
- ✅ Spring 可以針對不同層級做特殊處理（例如 @Repository 會轉換資料庫例外）

---

### 7. 依賴注入（Dependency Injection）

#### 定義
**依賴注入**：Spring 自動幫你建立物件，並注入到需要的地方。

#### 不用依賴注入（手動建立）

```java
public class AuthService {
    private UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();  // 手動 new
    }
}
```

**問題**：
- ❌ 緊密耦合（AuthService 依賴 UserRepository 的實作）
- ❌ 難以測試（無法用假的 UserRepository）
- ❌ 每次都要手動 new

---

#### 使用依賴注入（Spring 自動注入）

```java
@Service
public class AuthService {
    private final UserRepository userRepository;

    // Spring 自動注入 UserRepository
    @Autowired  // 可以省略（Spring 6+）
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**好處**：
- ✅ 鬆耦合（AuthService 不知道 UserRepository 怎麼實作）
- ✅ 容易測試（可以注入假的 UserRepository）
- ✅ Spring 自動管理物件生命週期

---

#### Lombok 簡化

```java
@Service
@RequiredArgsConstructor  // 自動產生建構子
public class AuthService {
    private final UserRepository userRepository;  // Spring 會自動注入
}
```

---

## 三、JWT 認證

### 8. JWT Token 是什麼？

#### 定義
**JWT (JSON Web Token)** 是一種無狀態的認證機制，用來證明「你是誰」和「你有什麼權限」。

#### 比喻：會員卡

| 概念 | 比喻 |
|------|------|
| JWT Token | 會員卡 |
| Token 內容 | 卡片上的資訊（姓名、等級、有效期限） |
| 簽名 | 防偽印章 |

---

#### JWT Token 結構

```
eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6ImVsdmlzIiwicm9sZSI6IkFETUlOIn0.signature
│                     │                                                      │
Header                Payload                                              Signature
（演算法）             （內容）                                              （簽名）
```

**Payload（內容）**：
```json
{
  "username": "elvis",
  "userId": 123,
  "email": "elvis@5dpapa.com",
  "role": "ADMIN",
  "exp": 1696755600
}
```

---

#### JWT 認證流程

```
1. 使用者登入
   → 後端驗證帳號密碼
   → 生成 JWT Token
   → 回傳給前端

2. 前端儲存 Token
   → localStorage 或 sessionStorage

3. 前端訪問 API
   → 帶著 Token：Authorization: Bearer <token>
   → 後端驗證 Token
   → 允許/拒絕訪問
```

---

### 9. JWT_SECRET 密鑰是給誰用的？

#### 重要觀念

**JWT_SECRET 不是使用者的密碼！**

| 密碼類型 | 誰擁有 | 用途 | 範例 |
|---------|--------|------|------|
| **使用者密碼** | 使用者 | 登入用 | `123456` |
| **JWT_SECRET** | 伺服器 | 簽名和驗證 Token | `XfK9mN2pQ7v...` |
| **JWT Token** | 使用者 | 訪問 API 用 | `eyJhbGciOi...` |

---

#### 密鑰的作用：防止偽造

**情境：駭客想偽造 Token**

```
駭客製作假 Token：
{
  "username": "hacker",
  "role": "ADMIN"
}

但是！駭客沒有 JWT_SECRET，無法正確簽名

後端收到假 Token：
  → 用 JWT_SECRET 驗證簽名
  → ❌ 簽名錯誤！
  → ❌ 拒絕訪問
```

**比喻**：
- JWT_SECRET = 政府的「公文大印」（只有政府有）
- 駭客沒有大印，無法偽造公文

---

#### 誰設定密鑰？

**✅ 開發人員/系統管理員**
```bash
# 開發階段
export JWT_SECRET="your-secret-key"
./mvnw spring-boot:run

# 正式環境
# DevOps 在伺服器設定環境變數
```

**❌ 使用者**
- 完全不知道密鑰存在
- 也不應該知道

---

### 10. Session Stateful vs Stateless

#### 方式 1：傳統 Session（有狀態 Stateful）

**比喻**：夜店用「簽到本」記錄誰進來了

```
客人登入：
1. 夜店驗證成功
2. 在「簽到本」寫：elvis 在 10:00 進來，是 VIP
3. 給客人「號碼牌」：NO.123

客人訪問 API：
1. 拿著號碼牌 NO.123
2. 保全查「簽到本」
3. 找到 NO.123 是 elvis，是 VIP
4. 允許進入
```

**資料儲存**：
- ✅ 伺服器（簽到本）
- ❌ 客人只有號碼牌

**問題**：
- ❌ 伺服器要一直保存「簽到本」（佔記憶體）
- ❌ 多台伺服器要同步「簽到本」

---

#### 方式 2：JWT（無狀態 Stateless）

**比喻**：夜店不用簽到本，所有資訊都在「會員卡」上

```
客人登入：
1. 夜店驗證成功
2. 製作「會員卡」（JWT Token）
   卡片上寫：elvis、VIP、有效期限
3. 給客人會員卡

客人訪問 API：
1. 拿著會員卡
2. 保全「讀取卡片」
3. 上面寫 elvis、VIP、未過期
4. 允許進入
```

**資料儲存**：
- ❌ 伺服器不儲存任何東西
- ✅ 客人自己帶著會員卡

**優點**：
- ✅ 伺服器不用記住任何東西（省記憶體）
- ✅ 多台伺服器不用同步
- ✅ 適合微服務

---

#### Spring Security 配置

```java
.sessionManagement(session ->
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
)
```

**意思**：
> 「Spring Security，我們用 JWT（無狀態），你不要幫我建立 Session。」

---

## 四、開發工具和文件

### 11. 如何閱讀 Spring 官方文件（JavaDoc）

#### JavaDoc 是什麼？
**JavaDoc** = Java 程式碼的「使用說明書」

**網址範例**：
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/OncePerRequestFilter.html

---

#### 文件結構

```
┌─────────────────────────────────────┐
│ 1. Class 名稱和繼承關係              │
├─────────────────────────────────────┤
│ 2. Class Description (類別說明)     │
├─────────────────────────────────────┤
│ 3. Constructor Summary (建構子摘要) │
├─────────────────────────────────────┤
│ 4. Method Summary (方法摘要)        │  ← 最重要！
├─────────────────────────────────────┤
│ 5. Method Detail (方法詳細說明)     │
└─────────────────────────────────────┘
```

---

#### 怎麼看 Method Summary？

**範例**：OncePerRequestFilter

| Modifier and Type | Method | Description |
|-------------------|--------|-------------|
| `protected abstract void` | `doFilterInternal(...)` | 必須實作 |
| `protected boolean` | `shouldNotFilter(...)` | 可選 |

**判斷方式**：
- `abstract` → ✅ **必須**實作
- 沒有 `abstract` → ❌ 可選

---

#### 實際使用步驟

1. **看 Method Summary** → 找到需要的方法
2. **點進方法** → 看詳細說明
3. **查看參數和回傳值** → 了解怎麼用
4. **寫程式碼**

---

### 12. 怎麼知道要用哪個 Spring 類別？

#### 真實流程

**❌ 不是這樣**：
```
打開 Spring 文件
→ 從頭讀到尾
→ 記住所有類別
→ 開始寫程式
```

**✅ 而是這樣**：
```
1. 有需求（例如：驗證 JWT）
   ↓
2. Google 搜尋
   "Spring Security JWT filter"
   ↓
3. 找到別人的範例
   大家都用 OncePerRequestFilter
   ↓
4. 查官方文件確認用法
   ↓
5. 驗證是否為業界標準
   ↓
6. 寫程式碼
```

---

#### 搜尋技巧

**好的搜尋**：
- `Spring Boot JWT filter example`
- `Spring Security authentication best practices`
- `Spring Data JPA repository naming conventions`

**不好的搜尋**：
- `Java filter`
- `how to check token`

**加上年份**：
- `Spring Security 6 JWT 2024`

---

### 13. Maven 是什麼？pom.xml 做什麼用？

#### Maven 是什麼？
**Maven** 是 Java 專案的「建置工具」和「依賴管理工具」。

**比喻**：
- 就像 JavaScript 的 npm
- 就像 Python 的 pip

---

#### pom.xml 的作用

**pom.xml** = Project Object Model（專案物件模型）

**內容**：
1. 專案資訊（名稱、版本）
2. 依賴項（需要哪些函式庫）
3. 建置設定（如何編譯）

**範例**：
```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.3</version>
    </dependency>
</dependencies>
```

**比喻**：
- pom.xml = package.json（npm）
- `<dependency>` = `"dependencies"`

---

#### Maven 常用指令

| 指令 | 作用 |
|------|------|
| `./mvnw clean` | 清除編譯檔案 |
| `./mvnw compile` | 編譯程式碼 |
| `./mvnw test` | 執行測試 |
| `./mvnw package` | 打包成 JAR |
| `./mvnw spring-boot:run` | 啟動 Spring Boot |

---

## 五、資料庫相關

### 14. JPA Entity 和 Repository 的關係

#### JPA Entity
**Entity** = 資料庫的「表格」在 Java 中的對應。

**範例**：
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
}
```

**對應的資料庫表格**：
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    email VARCHAR(100)
);
```

---

#### Repository
**Repository** = 資料存取層，負責與資料庫溝通。

**範例**：
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

**Spring Data JPA 會自動實作**：
- 不需要寫 SQL
- 根據方法名稱自動生成查詢

---

#### 關係圖

```
Controller（接收請求）
    ↓
Service（業務邏輯）
    ↓
Repository（資料存取）
    ↓
Database（資料庫）
```

---

### 15. Spring Data JPA 方法命名規則

#### 命名規則

Spring Data JPA 會根據方法名稱自動生成 SQL。

**前綴**：
- `findBy...` ← 最常用
- `existsBy...`
- `countBy...`
- `deleteBy...`

**組合**：
- `And` → `findByUsernameAndEmail(...)`
- `Or` → `findByUsernameOrEmail(...)`

**修飾詞**：
- `IgnoreCase` → 不區分大小寫
- `OrderBy...Asc/Desc` → 排序

---

#### 範例

| 方法名稱 | 生成的 SQL |
|---------|-----------|
| `findByUsername(String username)` | `SELECT * FROM users WHERE username = ?` |
| `findByUsernameAndEmail(...)` | `SELECT * FROM users WHERE username = ? AND email = ?` |
| `findByUsernameIgnoreCase(...)` | `SELECT * FROM users WHERE LOWER(username) = LOWER(?)` |
| `existsByEmail(String email)` | `SELECT COUNT(*) > 0 FROM users WHERE email = ?` |

---

#### 回傳型別

| 回傳型別 | 用途 | 範例 |
|---------|------|------|
| `Optional<User>` | 單一結果（推薦） | `findByUsername(...)` |
| `User` | 單一結果（可能 null） | `findByUsername(...)` |
| `List<User>` | 多筆結果 | `findByEnabled(...)` |
| `boolean` | 存在性檢查 | `existsByUsername(...)` |

---

## 六、實務問題

### 16. 為什麼啟動失敗？找不到環境變數

#### 錯誤訊息

```
Could not resolve placeholder 'JWT_SECRET' in value "${JWT_SECRET}"
```

#### 原因

**你的 JwtUtil.java**：
```java
@Component
public class JwtUtil {
    @Value("${JWT_SECRET}")
    private String secret;  // 需要從環境變數讀取
}
```

**Spring Boot 啟動時**：
1. 掃描到 `@Component`
2. 嘗試建立 `JwtUtil`
3. 看到 `@Value("${JWT_SECRET}")`
4. 去找 `JWT_SECRET` 環境變數
5. ❌ 找不到
6. ❌ 啟動失敗

---

#### 解決方法

**方法 1：設定環境變數**
```bash
export JWT_SECRET="your-secret-key"
export JWT_EXPIRATION_MS=3600000
export JWT_REFRESH_EXPIRATION_MS=604800000

./mvnw spring-boot:run
```

**方法 2：建立 .env 檔案**
```bash
# .env
JWT_SECRET=your-secret-key
JWT_EXPIRATION_MS=3600000
JWT_REFRESH_EXPIRATION_MS=604800000
```

**方法 3：在 application.properties 設定預設值**
```properties
JWT_SECRET=${JWT_SECRET:default-secret-for-dev}
JWT_EXPIRATION_MS=${JWT_EXPIRATION_MS:3600000}
```

**注意**：方法 3 不推薦用於正式環境！

---

### 17. CORS 是什麼？為什麼需要設定？

#### 定義
**CORS (Cross-Origin Resource Sharing)** = 跨域資源共享

#### 問題情境

**你的專案**：
- 前端：`https://elvis1056.github.io`（GitHub Pages）
- 後端：`http://localhost:8080`（本地開發）

**不同網域** → 瀏覽器會阻擋！

---

#### 瀏覽器的同源政策（Same-Origin Policy）

**同源定義**：協議、網域、Port 都相同

| URL 1 | URL 2 | 是否同源 |
|-------|-------|---------|
| `http://localhost:8080` | `http://localhost:8080/api` | ✅ 是 |
| `http://localhost:8080` | `http://localhost:3000` | ❌ 否（Port 不同） |
| `http://localhost:8080` | `https://localhost:8080` | ❌ 否（協議不同） |
| `http://example.com` | `http://api.example.com` | ❌ 否（子網域不同） |

---

#### 為什麼瀏覽器要阻擋？

**安全考量**：防止惡意網站竊取資料

**情境**：
```
你登入銀行網站：bank.com
惡意網站：evil.com

evil.com 的 JavaScript：
  fetch('https://bank.com/api/transfer', {
    method: 'POST',
    body: { to: 'hacker', amount: 10000 }
  })

如果沒有 CORS 限制：
  → 惡意網站可以用你的身份轉帳
  → 你的錢被偷了
```

**瀏覽器的保護**：
- ❌ 阻擋跨域請求
- ✅ 只有 bank.com 明確允許的網域才能訪問

---

#### CORS 設定（Spring Boot）

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // 允許的來源
    config.setAllowedOrigins(Arrays.asList(
        "https://elvis1056.github.io",  // 正式環境
        "http://localhost:3000"          // 本地開發
    ));

    // 允許的 HTTP 方法
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

    // 允許的 Header
    config.setAllowedHeaders(Arrays.asList("*"));

    // 允許帶 Cookie/Token
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

---

## 📚 延伸學習資源

### 官方文件
- Spring Boot Reference：https://docs.spring.io/spring-boot/reference/
- Spring Security Reference：https://docs.spring.io/spring-security/reference/
- Spring Data JPA：https://docs.spring.io/spring-data/jpa/reference/

### 推薦教學網站
- Baeldung：https://www.baeldung.com/
- Spring Guides：https://spring.io/guides
- JavaGuides：https://www.javaguides.net/

### 工具
- JWT Debugger：https://jwt.io/
- H2 Console：http://localhost:8080/h2-console（開發時）

---

**最後更新**: 2025-10-09
**作者**: elvis1056
**專案**: 5dpapa-backend
