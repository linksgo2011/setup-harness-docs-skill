# 领域模型业务事实

## 1. 聚合根总览

| 聚合 | 标识 | 业务意义 | 生命周期 |
|------|------|----------|----------|
| User | userId (UUID) | 平台用户/管理员 | 注册 → 活跃 → 禁用 |
| Consultant | consultantId (UUID) | 咨询服务提供者 | 创建 → 活跃 → 禁用 |
| Appointment | appointmentId (UUID) | 用户与咨询师之间的预约 | 待确认 → 已确认 → 已完成 / 已取消 |
| Branch | branchId (UUID) | 咨询网点/办公地点 | 创建 → 营业中 → 已关闭 |

## 2. User 聚合

| 字段 | 类型 | 业务约束 |
|------|------|----------|
| email | String | 唯一、登录凭证 |
| password | String | BCrypt 哈希，最少 8 位 |
| name | String | 显示名称 |
| phone | String | 可选 |
| role | UserRole 枚举 | ROLE_USER（默认）、ROLE_ADMIN |
| status | UserStatus 枚举 | ACTIVE（默认）、DISABLED |

业务不变条件：
- email 全局唯一
- 密码已加密存储，不可逆
- DISABLED 用户不可登录

## 3. Consultant 聚合

| 字段 | 类型 | 业务约束 |
|------|------|----------|
| name | String | 咨询师姓名 |
| title | String | 专业头衔（如"资深心理咨询师"） |
| bio | String | 个人简介 |
| specialties | String | 专长（逗号分隔） |
| avatar | String | 头像 URL |
| branchId | String | 关联网点 ID |
| status | ConsultantStatus | ACTIVE（可预约）、DISABLED（不可预约） |
| sortOrder | int | 前台展示排序（升序） |

业务不变条件：
- 仅 ACTIVE 状态的咨询师可被用户看到和被预约
- 显示顺序按 sortOrder 升序排列

## 4. Appointment 聚合

| 字段 | 类型 | 业务约束 |
|------|------|----------|
| userId | String | 预约人 |
| consultantId | String | 被预约的咨询师 |
| branchId | String | 网点（可选） |
| appointmentDate | LocalDate | 预约日期 |
| startTime | String (HH:mm) | 开始时间 |
| endTime | String (HH:mm) | 结束时间 |
| durationMinutes | int | 时长（分钟） |
| status | AppointmentStatus | PENDING/CONFIRMED/COMPLETED/CANCELLED |
| note | String | 用户备注（可选） |
| cancelReason | String | 取消原因 |

业务不变条件：
- 同一咨询师、同一日期、同一开始时间，至多只有一个非取消状态的预约
- 只能取消 PENDING 或 CONFIRMED 状态的预约

## 5. Branch 聚合

| 字段 | 类型 | 业务约束 |
|------|------|----------|
| name | String | 网点名称 |
| address | String | 地址 |
| phone | String | 联系电话 |
| status | BranchStatus | ACTIVE（营业中）、DISABLED（已关闭） |

## 6. 值对象

| 值对象 | 类型 | 值域 |
|--------|------|------|
| UserRole | 枚举 | ROLE_USER（普通用户）、ROLE_ADMIN（管理员） |
| UserStatus | 枚举 | ACTIVE（正常）、DISABLED（已停用） |
| AppointmentStatus | 枚举 | PENDING（待确认）、CONFIRMED（已确认）、COMPLETED（已完成）、CANCELLED（已取消） |
| TimeSlot | Record | date(LocalDate)、startTime(LocalTime)、endTime(LocalTime) |

## 7. 领域服务

| 服务 | 职责 |
|------|------|
| PasswordService | 密码加密（BCrypt）与校验 |
| AppointmentConflictService | 检测同一咨询师、同一日期、同一时段的预约冲突 |

## 8. 数据字典

| 字典类型 | 关联枚举 | 用途 |
|----------|----------|------|
| USER_ROLE | UserRole | 用户角色 |
| USER_STATUS | UserStatus | 用户状态 |
| APPOINTMENT_STATUS | AppointmentStatus | 预约状态 |
| CONSULTANT_STATUS | Consultant.ConsultantStatus | 咨询师状态 |
| BRANCH_STATUS | Branch.BranchStatus | 网点状态 |
