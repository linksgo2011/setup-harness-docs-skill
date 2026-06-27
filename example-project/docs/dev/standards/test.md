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

---

## 前端测试规范

| 层次 | 测试类型 | 框架 | 关注点 |
|------|----------|------|--------|
| View | E2E 测试 | Playwright | 完整用户流程、页面元素可见性 |
| Composable | 单元测试 | Vitest | 组合式函数逻辑 |
| Store | 单元测试 | Vitest + Pinia | 状态变更与异步 action |
| Component | 组件测试 | Vitest + Vue Test Utils | Props/Events/Slots |

### E2E 测试（Playwright）

- 测试文件位于 `e2e/tests/`，以 `.spec.ts` 结尾
- 运行命令：`bash e2e-run.sh`（启动前后端 + 执行测试）
- 测试用例需对应 `docs/qa/e2e-cases/` 中的文本用例
- 使用 Page Object Model 封装页面操作

### Unit 测试（Vitest）

- 测试文件与源码同目录，以 `.test.ts` 结尾
- Composable 测试：mock service 层，验证响应式状态
- Store 测试：使用 `setActivePinia(createPinia())` 隔离 store 实例
