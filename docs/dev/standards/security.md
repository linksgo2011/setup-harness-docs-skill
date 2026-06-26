# 安全规范

- 密码 BCrypt 加密，至少 8 位含字母+数字
- JWT HMAC-SHA256，24h 有效期，载荷含 userId/email/role
- 无状态鉴权，Token 传 Authorization: Bearer <token>
- 公开端点：/auth/**, /home/**, GET /consultants
- 管理端点：/admin/** 需 ROLE_ADMIN
