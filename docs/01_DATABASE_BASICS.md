# Database Basics (資料庫基礎)

**Target Audience**: Complete beginners
**Purpose**: Understand what databases are and how they work in this project

---

## 1. What is a Database? (什麼是資料庫？)

### 生活化比喻

**資料庫 = 超級強化版的 Excel**

| Excel | Database |
|-------|----------|
| 工作表 (Sheet) | 資料表 (Table) |
| 欄位 (Column A, B, C...) | 欄位 (Column/Field) |
| 每一列資料 | 每一筆記錄 (Row/Record) |
| 手動儲存 | 自動儲存 |
| 幾千筆資料就會卡 | 可以處理百萬、千萬筆 |
| 一次只能一個人編輯 | 可以同時幾百人存取 |

### 實際範例

**Excel 版本的使用者清單**：
```
| ID | Username | Password | Email              | Age |
|----|----------|----------|--------------------|-----|
| 1  | elvis    | pass123  | elvis@example.com  | 25  |
| 2  | john     | pass456  | john@example.com   | 30  |
| 3  | mary     | pass789  | mary@example.com   | 22  |
```

**Database 版本（一模一樣的結構，但更強大）**：
- 可以快速搜尋（例如：找出所有 Age > 25 的使用者）
- 可以設定規則（例如：Username 不能重複）
- 可以建立關聯（例如：每個使用者有多篇文章）
- 可以處理大量資料（百萬筆也不會慢）

---

## 2. Database Types (資料庫類型)

### 2.1 Relational Database (關聯式資料庫) - 我們用這個

**特色**：資料用「表格」儲存，表格之間可以建立關聯

**常見的關聯式資料庫**：
- **MySQL** - 最流行，免費，我們未來會用
- **PostgreSQL** - 功能強大，免費
- **H2** - 輕量級，可以跑在記憶體裡（我們現在用這個學習）
- Oracle、SQL Server - 商業版，要付費

**範例：使用者和文章的關聯**
```
Users Table (使用者表)
| user_id | username | email              |
|---------|----------|--------------------|
| 1       | elvis    | elvis@example.com  |
| 2       | john     | john@example.com   |

Posts Table (文章表)
| post_id | title        | content      | author_id (關聯到 user_id) |
|---------|--------------|--------------|----------------------------|
| 1       | My first post| Hello world  | 1                          |
| 2       | Java tutorial| Learn Java   | 1                          |
| 3       | SQL basics   | SQL guide    | 2                          |
```

**關聯的意義**：
- `author_id = 1` 代表這篇文章是 `elvis` 寫的
- 可以查詢「elvis 寫了哪些文章」
- 可以查詢「這篇文章是誰寫的」

### 2.2 NoSQL Database (非關聯式資料庫) - 我們不用

**特色**：資料用「JSON 文件」或「Key-Value」儲存

**常見的 NoSQL 資料庫**：
- MongoDB（文件型）
- Redis（Key-Value，常用於快取）

**為什麼我們不用？**
- 你的需求（會員、訂單、部落格）很適合用關聯式資料庫
- 關聯式資料庫更適合有「明確結構」的資料
- Spring Boot 預設支援關聯式資料庫

---

## 3. H2 Database (開發用)

### 3.1 What is H2?

**H2 = 可以跑在記憶體裡的資料庫**

**特色**：
- ✅ 不用安裝，直接用
- ✅ 啟動超快
- ✅ 有網頁介面可以看資料
- ❌ 重啟應用程式，資料就不見了（因為在記憶體裡）

**適合場景**：
- 學習階段（現在）
- 開發階段測試
- 單元測試

**不適合場景**：
- 正式上線（Production）
- 需要永久儲存資料

### 3.2 H2 vs MySQL Comparison

| Feature | H2 (開發用) | MySQL (正式用) |
|---------|------------|----------------|
| 安裝 | 不用安裝 | 需要安裝伺服器 |
| 資料保存 | 重啟就消失 | 永久保存 |
| 效能 | 一般 | 高效能 |
| 多人使用 | 只有你自己 | 可以幾百人同時用 |
| 學習難度 | 簡單 | 需要學設定 |

**我們的策略**：
1. **現在**：用 H2 學習和開發
2. **之後**：換成 MySQL 正式上線

---

## 4. SQL Basics (SQL 基礎語法)

SQL = Structured Query Language（結構化查詢語言）
**就是用來跟資料庫溝通的語言。**

### 4.1 Create Table (建立資料表)

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 主鍵，自動遞增
    username VARCHAR(50) NOT NULL UNIQUE,  -- 不能是 NULL，不能重複
    password VARCHAR(255) NOT NULL,        -- 不能是 NULL
    email VARCHAR(100),                    -- 可以是 NULL
    age INT DEFAULT 0                      -- 預設值是 0
);
```

**解釋**：
- `BIGINT`: 大整數（可以存很大的數字）
- `VARCHAR(50)`: 字串，最多 50 個字元
- `PRIMARY KEY`: 主鍵（唯一識別每筆資料）
- `AUTO_INCREMENT`: 自動遞增（1, 2, 3, 4...）
- `NOT NULL`: 不能是空值
- `UNIQUE`: 不能重複

### 4.2 Insert Data (新增資料)

```sql
INSERT INTO users (username, password, email, age)
VALUES ('elvis', 'encrypted_pass', 'elvis@example.com', 25);
```

結果：
```
| id | username | password        | email              | age |
|----|----------|-----------------|--------------------| ----|
| 1  | elvis    | encrypted_pass  | elvis@example.com  | 25  |
```

### 4.3 Select Data (查詢資料)

```sql
-- 查詢所有資料
SELECT * FROM users;

-- 查詢特定欄位
SELECT username, email FROM users;

-- 條件查詢
SELECT * FROM users WHERE age > 20;

-- 排序
SELECT * FROM users ORDER BY age DESC;  -- 由大到小

-- 限制數量
SELECT * FROM users LIMIT 10;  -- 只取前 10 筆
```

### 4.4 Update Data (更新資料)

```sql
UPDATE users
SET email = 'newemail@example.com'
WHERE id = 1;
```

### 4.5 Delete Data (刪除資料)

```sql
DELETE FROM users WHERE id = 1;
```

**⚠️ 注意**：沒有 `WHERE` 條件會刪除所有資料！
```sql
DELETE FROM users;  -- 危險！會刪除所有使用者
```

---

## 5. JPA (Java Persistence API)

### 5.1 What is JPA?

**JPA = 讓你不用寫 SQL，直接用 Java 物件操作資料庫**

**傳統方式（手寫 SQL）**：
```java
String sql = "SELECT * FROM users WHERE id = ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setLong(1, userId);
ResultSet rs = stmt.executeQuery();
// 還要手動把 ResultSet 轉成 User 物件... 超麻煩
```

**JPA 方式**：
```java
User user = userRepository.findById(userId).orElse(null);
// 就這樣，一行搞定！
```

### 5.2 Entity (實體類別)

**Entity = Java 物件對應到資料表**

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;  // 沒寫 @Column，會用預設值

    private int age;
}
```

**對應到的資料表**：
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    age INT
);
```

### 5.3 Repository (資料存取層)

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA 會自動實作這些方法！

    // 根據 username 查詢（方法名稱會自動轉成 SQL）
    Optional<User> findByUsername(String username);

    // 根據 email 查詢
    Optional<User> findByEmail(String email);

    // 查詢 age 大於某個值的所有使用者
    List<User> findByAgeGreaterThan(int age);

    // 根據 username 和 password 查詢
    Optional<User> findByUsernameAndPassword(String username, String password);
}
```

**Spring Data JPA 的魔法**：
- 你只要寫 interface，不用寫實作
- 方法名稱遵循規則，就會自動產生 SQL
- `findByUsername` → `SELECT * FROM users WHERE username = ?`
- `findByAgeGreaterThan` → `SELECT * FROM users WHERE age > ?`

---

## 6. Database Relationships (資料表關聯)

### 6.1 One-to-Many (一對多)

**範例：一個使用者可以寫多篇文章**

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // 一個 User 有多個 Post
    @OneToMany(mappedBy = "author")
    private List<Post> posts;
}

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    // 多個 Post 屬於一個 User
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
```

**資料表結構**：
```
users
| id | username |
|----|----------|
| 1  | elvis    |

posts
| id | title        | content      | author_id |
|----|--------------|--------------|-----------|
| 1  | First post   | Hello world  | 1         |
| 2  | Second post  | Learn Java   | 1         |
```

### 6.2 Many-to-Many (多對多)

**範例：一個訂單可以有多個商品，一個商品可以出現在多個訂單**

```java
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;
}

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
}
```

**資料表結構**：
```
orders
| id | order_date |
|----|------------|
| 1  | 2025-10-07 |

products
| id | name    | price  |
|----|---------|--------|
| 1  | iPhone  | 30000  |
| 2  | MacBook | 50000  |

order_products (中間表)
| order_id | product_id |
|----------|------------|
| 1        | 1          |
| 1        | 2          |
```

---

## 7. H2 Console 使用教學

### 7.1 啟動應用程式後，開啟 H2 Console

1. 啟動 Spring Boot: `mvn spring-boot:run`
2. 開啟瀏覽器：`http://localhost:8080/h2-console`
3. 設定連線資訊：
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (空白)
4. 點擊 "Connect"

### 7.2 在 H2 Console 執行 SQL

```sql
-- 查看所有 users
SELECT * FROM users;

-- 新增測試資料
INSERT INTO users (username, password, email, age)
VALUES ('testuser', 'testpass', 'test@example.com', 25);

-- 查詢特定使用者
SELECT * FROM users WHERE username = 'testuser';
```

---

## 8. Common Questions (常見問題)

### Q1: H2 資料為什麼重啟就不見了？
**A**: 因為 H2 跑在記憶體裡（in-memory），關掉程式記憶體就清空了。這是故意的，方便開發測試。

### Q2: 什麼時候要換成 MySQL？
**A**: 當你要正式上線，或需要永久儲存資料時。開發階段用 H2 比較方便。

### Q3: JPA 會自動建立資料表嗎？
**A**: 會！在開發階段我們設定 `spring.jpa.hibernate.ddl-auto=create-drop`，每次啟動會自動建表。

### Q4: Repository 的方法名稱有什麼規則？
**A**:
- `findBy` 開頭 + 欄位名稱 = 查詢
- `And` = 多個條件
- `Or` = 或條件
- `GreaterThan` / `LessThan` = 大於/小於
- `Like` = 模糊搜尋

範例：
```java
findByUsernameAndAge(String username, int age)
// → SELECT * FROM users WHERE username = ? AND age = ?

findByUsernameLike(String pattern)
// → SELECT * FROM users WHERE username LIKE ?
```

### Q5: 資料庫密碼要加密嗎？
**A**: **絕對要！** 我們會用 BCrypt 加密，絕對不能存明文密碼。

---

## 9. Next Steps (下一步)

看完這份文件後，你應該要理解：
- [x] 資料庫是什麼
- [x] H2 和 MySQL 的差異
- [x] SQL 基本語法
- [x] JPA Entity 和 Repository 的概念
- [x] 資料表關聯（一對多、多對多）

**接下來**：
1. 看 `API_TESTING.md` 學習如何測試 API
2. 看 `UNIT_TESTING.md` 學習如何寫測試
3. 看 `SECURITY_BASICS.md` 學習安全性概念

**如果有任何不懂的地方，隨時問我！**
