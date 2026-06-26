# ADR-005: 当前用户上下文采用 ThreadLocal 传递

- **状态**: 已接受
- **日期**: 2026-06-19

## 上下文

应用层和领域层的方法需要获取当前登录用户信息（userId、email、role）。需要决定传递方式。

## 决策

使用 `UserContextHolder`（ThreadLocal） + `SecurityUtils` 静态工具类获取当前用户上下文。

- `JwtAuthenticationFilter` 从请求头解析 JWT → 写入 `SecurityContextHolder` + `UserContextHolder`
- `UserContextHolder` 在请求结束时（`finally`块）清除 ThreadLocal

## 备选方案

| 方案 | 理由 | 否决原因 |
|------|------|----------|
| 方法参数显式传递 userId | 最干净 | 几乎所有方法都需要 userId，接口签名冗余 |
| Spring Security Authentication | 标准做法 | 领域层不应依赖 Spring Security API |
| 请求作用域 Bean | 自动注入 | 不直观，需要理解 Spring 作用域机制 |

## 后果

**正面**：
- 应用层/领域层无需在方法签名中传递 userId
- 不依赖 Spring Security API（只依赖 `UserContext` 值对象）
- ThreadLocal 在请求结束后自动清理，无内存泄漏

**负面**：
- ThreadLocal 隐式依赖需注意测试时设置上下文
- 异步操作（如 `@Async`）需手动传递上下文
- 需要 `SecurityUtils` 工具类做间接访问
