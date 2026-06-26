# 测试规范

## 分层测试策略

| 层次 | 测试类型 | 框架 | 关注点 |
|------|----------|------|--------|
| Adapter | 集成测试 | SpringBootTest + RestAssured | 请求/响应/状态码/错误码 |
| Application | 单元测试 | JUnit 5 + Mockito | 用例编排/事务边界 |
| Domain | 单元测试 | JUnit 5 | 业务不变量/状态机 |
| Infrastructure | 集成测试 | SpringBootTest + H2 | 持久化/映射正确性 |

## Adapter 层集成测试约定

- 基类：`TestBase` 继承 `@SpringBootTest(webEnvironment = RANDOM_PORT)`
- HTTP 客户端：RestAssured（配置端口 + 日志）
- 数据隔离：`@BeforeEach` 重置所有表
- 认证：使用 `loginAsUser()` / `loginAsAdmin()` 辅助方法获取 Token

## 测试命名

- 类名：`{被测Controller}Test`（如 `AuthControllerTest`）
- 方法名：`should_{action}_{expectedResult}`（如 `should_register_and_return_token`）

## 覆盖要求

每个 Controller 测试至少包含：
- 1 个 happy path
- 2 个错误分支

## H2 测试数据库配置

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;MODE=MySQL
    driver-class-name: org.h2.Driver
  flyway:
    enabled: false
```
