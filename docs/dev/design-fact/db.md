# 数据库设计

> 所有表使用 InnoDB 引擎，字符集 utf8mb4，排序 utf8mb4_bin。

## 公共字段

| 字段 | 类型 | 说明 |
|------|------|------|
| created_by | VARCHAR(64) | 创建者 ID |
| created_time | DATETIME | 创建时间，默认 CURRENT_TIMESTAMP |
| updated_by | VARCHAR(64) | 最后修改者 ID |
| updated_time | DATETIME | 最后修改时间，默认 CURRENT_TIMESTAMP，ON UPDATE 自动更新 |

## user — 用户表

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | VARCHAR(64) | PK | UUID 主键 |
| email | VARCHAR(100) | UNIQUE, NOT NULL | 邮箱 |
| password | VARCHAR(255) | NOT NULL | BCrypt 加密密码 |
| name | VARCHAR(50) | NOT NULL | 姓名 |
| phone | VARCHAR(20) | | 手机号 |
| role | VARCHAR(20) | NOT NULL, DEFAULT 'ROLE_USER' | 角色 |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | 状态 |

**索引**：`uk_user_email` UNIQUE on (email)

## consultant — 咨询师表

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | VARCHAR(64) | PK | UUID 主键 |
| name | VARCHAR(50) | NOT NULL | 姓名 |
| title | VARCHAR(100) | NOT NULL | 职称 |
| bio | TEXT | | 简介 |
| specialties | VARCHAR(255) | | 专长领域 |
| avatar | VARCHAR(255) | | 头像 URL |
| branch_id | VARCHAR(64) | FK → branch(id) | 所属网点 |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | 状态 |
| sort_order | INT | NOT NULL, DEFAULT 0 | 排序权重 |

## appointment — 预约表

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | VARCHAR(64) | PK | UUID 主键 |
| user_id | VARCHAR(64) | FK → user(id) | 预约用户 |
| consultant_id | VARCHAR(64) | FK → consultant(id) | 咨询师 |
| branch_id | VARCHAR(64) | FK → branch(id) | 所属网点 |
| appointment_date | DATE | NOT NULL | 预约日期 |
| start_time | VARCHAR(5) | NOT NULL | 开始时间 (HH:mm) |
| end_time | VARCHAR(5) | NOT NULL | 结束时间 (HH:mm) |
| duration_minutes | INT | NOT NULL | 时长（分钟） |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDING' | 状态 |
| note | VARCHAR(500) | | 备注 |
| cancel_reason | VARCHAR(200) | | 取消原因 |

**索引**：
- `uk_appointment_slot` UNIQUE on (consultant_id, appointment_date, start_time)
- `idx_user_id` on (user_id)
- `idx_consultant_date` on (consultant_id, appointment_date)

## branch — 网点表

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | VARCHAR(64) | PK | UUID 主键 |
| name | VARCHAR(100) | NOT NULL | 网点名称 |
| address | VARCHAR(255) | | 地址 |
| phone | VARCHAR(20) | | 电话 |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | 状态 |

## 表关系图

```
user (1) ──→ (N) appointment (N) ←── (1) consultant (N) ←── (1) branch
```
