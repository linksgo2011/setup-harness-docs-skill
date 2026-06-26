# ADR-004: 采用 JWT HMAC-SHA256 无状态鉴权

- **状态**: 已接受
- **日期**: 2026-06-19

## 上下文

前后端分离架构需要鉴权方案。需要决定使用 Session/Cookie 还是 Token（JWT）。

## 决策

后端自行签发与校验 JWT（HMAC-SHA256，24h 过期），无状态鉴权。

JWT Payload：
- `sub`: userId
- `email`: 用户邮箱
- `role`: ROLE_USER / ROLE_ADMIN

## 备选方案

| 方案 | 理由 | 否决原因 |
|------|------|----------|
| Session + Cookie | Spring Security 默认方案 | 需 Redis 存储 session、跨域配置复杂、不适合前后端分离 |
| OAuth2 / OIDC | 标准协议 | 增加 IDP 依赖，小型项目不需要 |
| JWT HMAC-SHA256 | 无状态、无额外存储 | 无法主动使 token 失效 |

## 后果

**正面**：
- 无状态、无需 Redis
- 前后端分离友好（`Authorization: Bearer <token>`）
- 后端自行签发，无外部依赖

**负面**：
- Token 签发后无法主动吊销（24h 内有效）
- 密钥保存在配置中，泄漏风险需注意
- 载荷明文字段不宜存放敏感信息
