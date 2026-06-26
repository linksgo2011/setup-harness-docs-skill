# 错误码清单

> 本文档描述咨询预约平台的错误码体系事实（as-built）。

## 错误码结构

```
{ErrorGroup}_{SubCode}
```

- ErrorGroup（错误组）：错误的来源分类
- SubCode（子码）：具体错误标识，可读性强

## ErrorGroup 定义

| Group | HTTP Status | 说明 |
|-------|-------------|------|
| COMMON | 400/404 | 通用参数/资源错误 |
| AUTH | 401/400 | 认证/鉴权错误 |
| BUSINESS | 409/400 | 业务逻辑冲突/错误 |
| SYSTEM | 500 | 系统内部错误 |

## 全量错误码

### COMMON 组

| ErrorCode | HTTP Status | 说明 | 触发场景 |
|-----------|-------------|------|----------|
| COMMON_PARAM_ERROR | 400 | 参数校验失败 | @Valid 校验不通过 |
| COMMON_RESOURCE_NOT_FOUND | 404 | 资源不存在 | 根据 ID 查询无结果 |
| COMMON_VALIDATION_ERROR | 400 | 参数格式错误 | 字段级校验失败 |

### AUTH 组

| ErrorCode | HTTP Status | 说明 | 触发场景 |
|-----------|-------------|------|----------|
| AUTH_EMAIL_EXISTS | 400 | 邮箱已注册 | 重复 email 调用 register |
| AUTH_WEAK_PASSWORD | 400 | 密码强度不足 | 密码 < 8 位 |
| AUTH_INVALID_CREDENTIALS | 400 | 邮箱或密码错误 | login 时密码不匹配 |
| AUTH_INVALID_TOKEN | 401 | Token 无效或过期 | JWT 解析失败 |
| AUTH_TOKEN_EXPIRED | 401 | Token 已过期 | JWT 超时 |
| AUTH_FORBIDDEN | 403 | 无权限 | 非 ADMIN 访问 /admin |
| AUTH_USER_DISABLED | 401 | 用户已被停用 | status == DISABLED |

### BUSINESS 组

| ErrorCode | HTTP Status | 说明 | 触发场景 |
|-----------|-------------|------|----------|
| BUSINESS_CONFLICT_SLOT | 409 | 该时段已被预约 | 同一咨询师同一时段重复预约 |
| BUSINESS_CONSULTANT_UNAVAILABLE | 400 | 咨询师不可预约 | consultant.isAvailable() == false |
| BUSINESS_APPOINTMENT_CANNOT_CANCEL | 409 | 当前状态不允许取消 | status 为 COMPLETED 或 CANCELLED |
| BUSINESS_APPOINTMENT_NOT_OWNER | 403 | 非本人预约不可操作 | userId 不匹配 |
| BUSINESS_USER_DISABLED | 400 | 用户已被停用 | 尝试操作已 DISABLED 的用户 |
| BUSINESS_BRANCH_HAS_CONSULTANTS | 409 | 网点有咨询师不可删除 | branch 关联的 consultant > 0 |

### SYSTEM 组

| ErrorCode | HTTP Status | 说明 | 触发场景 |
|-----------|-------------|------|----------|
| SYSTEM_INTERNAL_ERROR | 500 | 系统内部错误 | 未捕获的异常 |
| SYSTEM_DB_ERROR | 500 | 数据库错误 | SQL 执行失败 |

## 返回格式

```json
{
  "errorCode": "BUSINESS_CONFLICT_SLOT",
  "errorMessage": "该时间段已被预约",
  "data": null
}
```

## 当前实现映射

当前 `GlobalExceptionHandler` 使用简化 errorCode（无 ErrorGroup 前缀），映射关系：

| 当前 errorCode | 规范 errorCode | 异常类型 |
|----------------|---------------|----------|
| PARAM_ERROR | COMMON_PARAM_ERROR | IllegalArgumentException |
| VALIDATION_ERROR | COMMON_VALIDATION_ERROR | MethodArgumentNotValidException |
| BUSINESS_CONFLICT | BUSINESS_CONFLICT_SLOT | IllegalStateException |
| SYSTEM_ERROR | SYSTEM_INTERNAL_ERROR | Exception |

**迁移计划**：Phase 2 后统一改用规范 errorCode 格式。
