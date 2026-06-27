# 开发（Development）

此目录记录开发规范、执行方案、架构决策与系统设计现状。

## 目录

| 目录 | 用途 |
|------|------|
| `standards/` | 开发规范（分层、API、数据库、安全、命名、错误、测试等） |
| `plans/` | 每轮任务的执行方案与变更记录 |
| `adr/` | 架构决策记录（Architecture Decision Records） |
| `design-fact/` | 系统当前设计现状（as-built architecture） |
| `snippet/` | 常见业务代码打样模版 |

### 跨层覆盖

后端（Spring Boot）与前端（Vue 3）的规范均整合在同名文件中：

| 规范文件 | 后端内容 | 前端内容 |
|----------|----------|----------|
| `layers.md` | DDD 四层架构 | Vue 3 组件/视图/Composable/Store 分层 |
| `api.md` | RESTful 接口规范 | Axios 调用模式、拦截器 |
| `test.md` | JUnit 5 + RestAssured | Playwright E2E + Vitest |
| `security.md` | JWT / Spring Security | Token 存储、路由守卫、Axios 拦截 |
| `naming.md` | Java 命名 | Vue/TS 命名 |
| `error.md` | 后端异常体系 | 前端错误处理与用户提示 |
