# 本地 PostgreSQL 開發環境設定

## 為什麼要在開發環境用 PostgreSQL？

- ✅ **環境一致性**：開發和生產使用同一種資料庫
- ✅ **避免 SQL 方言差異**：H2 和 PostgreSQL 的 SQL 語法有差異
- ✅ **可測試 PostgreSQL 特有功能**：如 JSONB、全文檢索等
- ✅ **更接近真實場景**：早期發現問題

## 方案比較

| 方案 | 優點 | 缺點 | 適合誰 |
|------|------|------|--------|
| **H2 記憶體** | 快速、輕量 | 資料庫不一致 | 快速開發、測試 |
| **PostgreSQL (Docker)** | 環境一致、真實 | 需要 Docker | 正式開發、團隊協作 |

## 使用方式

### 方式 1：用 Docker Compose（推薦）

#### 啟動完整環境（PostgreSQL + Spring Boot）

```bash
# 第一次啟動（會建置 image）
docker-compose up --build

# 之後啟動（不需要重新建置）
docker-compose up

# 背景執行
docker-compose up -d

# 停止
docker-compose down

# 停止並刪除資料（重新開始）
docker-compose down -v
```

#### 只啟動 PostgreSQL（自己跑 Spring Boot）

```bash
# 只啟動資料庫
docker-compose up postgres

# 然後在另一個終端機執行 Spring Boot
./mvnw spring-boot:run
```

**環境變數會自動設定：**
- `DATABASE_URL=jdbc:postgresql://postgres:5432/fivepapa_dev`
- `DATABASE_USERNAME=fivepapa_user`
- `DATABASE_PASSWORD=dev_password_123`
- `DDL_AUTO=update`（保留資料）

---

### 方式 2：不用 Docker（降級到 H2）

```bash
# 直接執行，會使用 H2 記憶體資料庫
./mvnw spring-boot:run

# 訪問 H2 Console
http://localhost:8080/h2-console
```

**預設值（fallback）：**
- `DATABASE_URL=jdbc:h2:mem:testdb`
- `DATABASE_USERNAME=sa`
- `DATABASE_PASSWORD=`（空白）
- `DDL_AUTO=create-drop`（重啟清空）

---

## 連接到 PostgreSQL

### 使用 psql（命令列）

```bash
# 進入 PostgreSQL 容器
docker exec -it fivepapa-postgres psql -U fivepapa_user -d fivepapa_dev

# 常用指令
\dt               # 列出所有表格
\d users          # 查看 users 表格結構
SELECT * FROM users;  # 查詢資料
\q                # 離開
```

### 使用 GUI 工具（推薦）

#### DBeaver（免費、跨平台）

1. 下載：https://dbeaver.io/
2. 新增連線：
   - Host: `localhost`
   - Port: `5432`
   - Database: `fivepapa_dev`
   - Username: `fivepapa_user`
   - Password: `dev_password_123`

#### pgAdmin（免費）

1. 下載：https://www.pgadmin.org/
2. 設定同上

#### TablePlus（付費，但 UI 很美）

1. 下載：https://tableplus.com/
2. 設定同上

---

## 資料持久化

### PostgreSQL（Docker Compose）

```bash
# 資料會保存在 Docker Volume
docker volume ls | grep postgres_data

# 查看資料位置
docker volume inspect myprojectbackend_postgres_data

# 刪除所有資料（重新開始）
docker-compose down -v
```

**重啟容器：**
- ✅ 資料保留（因為用了 volume）
- ✅ 不會重新建表（因為 `ddl-auto=update`）

### H2

```bash
# 每次重啟：
# ❌ 資料全部消失（因為 ddl-auto=create-drop）
# ❌ 記憶體資料庫（容器停止就清空）
```

---

## 切換資料庫

### 從 H2 切換到 PostgreSQL

```bash
# 停止目前的容器
docker stop my-backend
docker rm my-backend

# 啟動 Docker Compose
docker-compose up
```

### 從 PostgreSQL 切換回 H2

```bash
# 停止 Docker Compose
docker-compose down

# 直接執行 Spring Boot
./mvnw spring-boot:run
```

---

## 常見問題

### Q1: Port 5432 已被佔用

```bash
# 檢查是否已安裝 PostgreSQL
lsof -i :5432

# 如果有其他 PostgreSQL 在跑，可以停止它
brew services stop postgresql

# 或者修改 docker-compose.yml 的 port
ports:
  - "5433:5432"  # 改成 5433
```

### Q2: 容器啟動失敗

```bash
# 查看 logs
docker-compose logs backend
docker-compose logs postgres

# 重新建置
docker-compose down
docker-compose build --no-cache
docker-compose up
```

### Q3: 資料庫連線失敗

```bash
# 確認 PostgreSQL 是否啟動
docker-compose ps

# 測試連線
docker exec -it fivepapa-postgres pg_isready -U fivepapa_user

# 查看網路
docker network ls
docker network inspect myprojectbackend_default
```

### Q4: 想清空所有資料重新開始

```bash
# 方法 1：刪除 volume
docker-compose down -v

# 方法 2：手動刪除資料
docker exec -it fivepapa-postgres psql -U fivepapa_user -d fivepapa_dev
DROP TABLE users;
\q
```

---

## 開發流程建議

### 日常開發

```bash
# 1. 早上啟動開發環境
docker-compose up -d

# 2. 查看 logs（可選）
docker-compose logs -f backend

# 3. 開發...

# 4. 晚上關閉（保留資料）
docker-compose down
```

### 測試新功能

```bash
# 1. 啟動乾淨的資料庫
docker-compose down -v
docker-compose up

# 2. 測試...

# 3. 滿意後提交程式碼
git add .
git commit -m "feat: add new feature"
```

### 快速原型開發

```bash
# 用 H2 快速測試
./mvnw spring-boot:run

# 確定沒問題後，用 PostgreSQL 再測一次
docker-compose up
```

---

## 效能比較

| 操作 | H2 | PostgreSQL (Docker) |
|------|-----|---------------------|
| 啟動時間 | ~3 秒 | ~5 秒 |
| 查詢速度 | 極快（記憶體） | 快（SSD） |
| 資料持久化 | ❌ | ✅ |
| 環境一致性 | ❌ | ✅ |

---

## 最佳實踐

1. ✅ **團隊協作**：用 PostgreSQL + Docker Compose
2. ✅ **個人快速開發**：用 H2
3. ✅ **寫完功能後**：用 PostgreSQL 再測一次
4. ✅ **CI/CD**：用 PostgreSQL（Docker）
5. ✅ **生產環境**：PostgreSQL（雲端）

---

## 下一步

- [ ] 學習 Flyway 資料庫遷移工具
- [ ] 設定 CI/CD 自動測試
- [ ] 學習 PostgreSQL 特有功能（JSONB、全文檢索）
