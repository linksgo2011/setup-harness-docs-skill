# 业务规则清单（事实方案）

> 所有业务规则通过 `IBusinessRule` 接口定义，可通过 `GET /api/v1/business-rules` 动态查询。
> 规则配置存储在 `resources/business-rules/*.yml` 中。

## 认证规则

| 规则名 | 描述 | 当前值 | 配置路径 |
|--------|------|--------|----------|
| auth.password.minLength | 密码最小长度 | 8 | `auth.yml` → password.minLength |
| auth.token.expirationMs | JWT Token 有效期（毫秒） | 86400000 (24h) | `application.yml` → jwt.expiration |
| auth.password.algorithm | 密码加密算法 | BCrypt | SecurityConfig (hardcoded) |

## 预约规则

| 规则名 | 描述 | 当前值 | 代码位置 |
|--------|------|--------|----------|
| appointment.conflict.detection | 同一咨询师同一时段不可重复预约 | 唯一索引 + 代码校验 | AppointmentConflictService + uk_appointment_slot |
| appointment.slot.minMinutes | 最短预约时长 | 30 (implicit) | TimeSlot 业务约定 |
| appointment.slot.maxMinutes | 最长预约时长 | 120 (implicit) | TimeSlot 业务约定 |
| appointment.available.start | 每日可预约开始时间 | 09:00 | ConsultantService.getAvailableSlots |
| appointment.available.end | 每日可预约结束时间 | 18:00 | ConsultantService.getAvailableSlots |
| appointment.slot.intervalMinutes | 时间段间隔 | 60 | ConsultantService.getAvailableSlots |

## 管理规则

| 规则名 | 描述 | 当前值 | 代码位置 |
|--------|------|--------|----------|
| admin.access.role | 管理后台权限角色 | ROLE_ADMIN | SecurityConfig + JwtAuthenticationFilter |
| user.status.toggle | 用户启用/停用 | 允许，不可操作自身 | AdminService.toggleUserStatus |
| branch.delete.guard | 删除网点前检查关联咨询师 | 暂未实现 | AdminService (待补充) |

## 字典规则

| 规则名 | 描述 | 当前值 | 代码位置 |
|--------|------|----------|----------|
| data.dictionary.queryable | 数据字典可通过 API 查询 | 所有注册的 DataDictionaryType | DataDictionaryController (Phase 3) |
| enum.self.describing | 所有业务枚举实现 Description 接口 | 提供 getCode() / getDescription() | 各 enum 文件 (Phase 3) |
