# API 规范

## 基础

- RESTful 风格
- URL 一律小写连字符（kebab-case）
- 名词使用复数（如 `/users`、`/appointments`）
- API 前缀 `/api/v1/`

## 请求

- GET/DELETE 参数通过 Query String
- POST/PUT 参数通过 JSON Body
- Content-Type: `application/json`
- 认证通过 `Authorization: Bearer <token>` 请求头

## 响应

- 成功：直接返回数据体
- 错误：统一格式 `{errorCode, errorMessage, data}`

## HTTP 状态码

| 状态码 | 含义 | 使用场景 |
|--------|------|----------|
| 200 | 成功 | 正常返回 |
| 400 | 参数错误 | 参数校验失败 / 业务参数错误 |
| 401 | 未授权 | Token 缺失 / 无效 / 过期 |
| 403 | 无权限 | 非管理员访问 /admin |
| 404 | 不存在 | 资源 ID 不存在 |
| 409 | 业务冲突 | 重复预约 / 状态冲突 |
| 500 | 服务器错误 | 未捕获异常 |

## 分页

- 入参：`page`（从 1 开始）、`pageSize`（默认 20）
- 出参：`{records, total, page, pageSize}`
