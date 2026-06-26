# 领域模型设计

> 本文档描述咨询预约平台的领域模型事实（as-built）。

## 聚合根 (Aggregate Root)

### User

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | UUID |
| email | String | 邮箱（登录名），唯一 |
| password | String | BCrypt 编码 |
| name | String | 姓名 |
| phone | String | 手机号 |
| role | UserRole | ROLE_USER / ROLE_ADMIN |
| status | UserStatus | ACTIVE / DISABLED |

**方法**：
- `isAdmin()` — role == ROLE_ADMIN
- `isActive()` — status == ACTIVE

### Consultant

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | UUID |
| name | String | 姓名 |
| title | String | 职称 |
| bio | String | 简介 |
| specialties | String | 专长（逗号分隔） |
| avatar | String | 头像 URL |
| branchId | String | 所属网点 ID |
| status | ConsultantStatus | ACTIVE / DISABLED |
| sortOrder | int | 排序权重 |

**内联枚举**：`ConsultantStatus { ACTIVE, DISABLED }`

**方法**：
- `isAvailable()` — status == ACTIVE

### Appointment

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | UUID |
| userId | String | 预约用户 ID |
| consultantId | String | 咨询师 ID |
| branchId | String | 网点 ID（可选） |
| appointmentDate | LocalDate | 预约日期 |
| startTime | String | 开始时间 HH:mm |
| endTime | String | 结束时间 HH:mm |
| durationMinutes | int | 时长 |
| status | AppointmentStatus | PENDING / CONFIRMED / COMPLETED / CANCELLED |
| note | String | 用户备注 |
| cancelReason | String | 取消原因 |

**方法**：
- `isOwnedBy(userId)` — 判断是否属于该用户
- `canCancel()` — status 不是 COMPLETED 也不是 CANCELLED

### Branch

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String | UUID |
| name | String | 网点名称 |
| address | String | 地址 |
| phone | String | 电话 |
| status | BranchStatus | ACTIVE / DISABLED |

**内联枚举**：`BranchStatus { ACTIVE, DISABLED }`

## 值对象 (Value Object)

| 类 | 字段 | 说明 |
|----|------|------|
| UserRole | ROLE_USER, ROLE_ADMIN | 用户角色枚举 |
| UserStatus | ACTIVE, DISABLED | 用户状态枚举 |
| AppointmentStatus | PENDING, CONFIRMED, COMPLETED, CANCELLED | 预约状态枚举 |
| TimeSlot | date, startTime, endTime | 时间段 VO，用于冲突检测 |

## 领域服务 (Domain Service)

| 服务 | 方法 | 说明 |
|------|------|------|
| PasswordService | encode(raw), matches(raw, encoded) | 密码编码/校验接口（infrastructure 实现 BCrypt） |
| AppointmentConflictService | validateNoConflict(existing, requested) | 校验同一咨询师同一时段无冲突预约 |

## 仓储接口 (Repository)

| 接口 | 核心方法 | 说明 |
|------|----------|------|
| UserRepository | findById, findByEmail, findAll, save, update, existsByEmail | 用户持久化 |
| ConsultantRepository | findById, findAllActive, findAll, save, update, deleteById, count | 咨询师持久化 |
| AppointmentRepository | findById, findByUserId, findByConsultantIdAndDate, findAll, save, update, count, countByStatus, countByUserId | 预约持久化 |
| BranchRepository | findById, findAll, save, update, deleteById, count | 网点持久化 |

## 聚合关系

```
User (1) ──→ (N) Appointment (N) ←── (1) Consultant (N) ←── (1) Branch
```

- User 创建 Appointment，Appointment 关联一个 User 和一个 Consultant
- Consultant 属于一个 Branch
- Branch 独立创建/停用，Consultant 依赖 Branch 存在
