---
name: dev-build
description: 遵循四层 DDD 架构实现功能。适用于为咨询预约平台编写后端或前端代码的场景。
---

# Dev 构建

按照 AGENTS.md 中的架构约定编写代码。

## 后端

- **adapter/rest/** — 控制器：仅做协议转换，不含业务逻辑
- **application/service/** — 应用服务：用例编排，`@Transactional`
- **domain/** — 聚合、值对象、领域服务：业务不变量
- **infrastructure/** — 持久化、安全、配置

依赖方向：adapter → application → domain ← infrastructure

## 前端

- Vue 3 Composition API + `<script setup>`
- Pinia store 管理认证状态（`src/stores/auth.ts`）
- 视图位于 `src/views/user/` 或 `src/views/admin/`
- Axios 拦截器处理 JWT 和 401 重定向

## 验证

```bash
cd backend && mvn compile
cd frontend && npm run build
```
