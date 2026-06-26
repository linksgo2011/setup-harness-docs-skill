# 安全设计

> 本文档描述咨询预约平台的安全架构事实（as-built）。

## JWT Token

**算法**：HMAC-SHA256（`jjwt` 库 MAC 算法）

**密钥**：通过 `application.yml` 的 `jwt.secret` 配置，至少 256 位

**有效期**：24 小时（`jwt.expiration` 配置，单位毫秒）

**Payload Claims**：

| Claim | 类型 | 值 | 说明 |
|-------|------|-----|------|
| sub | String | userId | 用户 UUID |
| email | String | email | 用户邮箱 |
| role | String | ROLE_USER / ROLE_ADMIN | 角色（与 Spring Security `hasRole()` 兼容） |
| iat | Date | 签发时间戳 | -
| exp | Date | 过期时间戳 | -

## JWT 处理流程

```
请求 → JwtAuthenticationFilter (OncePerRequestFilter)
        1. 从 Authorization: Bearer <token> 提取 Token
        2. 解析 JWT → userId + email + role
        3. 设置 SecurityContextHolder (Spring Security)
        4. 设置 UserContextHolder (自定义 ThreadLocal)
        5. 放行请求
        6. 请求结束后清除 ThreadLocal
```

## Filter Chain 顺序

```
Spring Security Filter Chain (SecurityConfig):
  1. JwtAuthenticationFilter (自定义, 提取 JWT 并设置上下文)
  2. UsernamePasswordAuthenticationFilter (内置, 不启用)
  3. ExceptionTranslationFilter (内置, 处理 AccessDenied)
  4. ...
```

## SecurityConfig 规则矩阵

| 路径模式 | 访问要求 |
|----------|----------|
| POST /api/v1/auth/register | 完全公开 |
| POST /api/v1/auth/login | 完全公开 |
| GET /api/v1/home/info | 完全公开 |
| GET /api/v1/data-dictionaries** | 完全公开 |
| GET /api/v1/business-rules** | 完全公开 |
| GET /api/v1/consultants** | 完全公开 |
| /admin/** | 仅 ROLE_ADMIN |
| /api/v1/** | 需要认证（任何角色） |

## BCrypt 密码策略

- 编码器：`BCryptPasswordEncoder`（Spring Security 默认实现）
- 强度：10（默认 rounds）
- 密码要求：至少 8 位，由前端校验

## UserContext ThreadLocal

**类**：`UserContextHolder` — `ThreadLocal<UserContext>`

**UserContext 字段**：

| 字段 | 类型 | 来源 |
|------|------|------|
| userId | String | JWT sub |
| email | String | JWT email |
| role | String | JWT role |

**获取**：`SecurityUtils.currentUserId()` — 从 UserContextHolder 获取当前用户 ID

## CORS 配置

- 当前：`WebConfig.java` 允许所有 origin（开发阶段）
- 生产：需限制为具体域名

## 密码修改流程

```
1. 用户输入旧密码 + 新密码
2. 从 DB 获取当前 BCrypt hash
3. passwordService.matches(旧密码, 当前 hash) → 校验
4. passwordService.encode(新密码) → 更新 DB
```
