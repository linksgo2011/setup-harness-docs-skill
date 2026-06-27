# 架构与工程规范（咨询预约单体应用）

本文件定义本代码库的架构边界、分层规则、模块职责与工程规范。目标是让新成员与自动化工具在不了解上下文的情况下，也能按一致方式新增功能、扩展模块并避免破坏依赖关系。

## 1. 仓库拓扑

这是一个全栈工程（Java 17 / Spring Boot 3.2.x / Vue 3 + TypeScript），主要模块如下：

- `backend/`：Spring Boot 后端（四层 DDD 单体）
- `frontend/`：Vue 3 + TypeScript 前端（Vite 构建）

运行时形态：

- Client → Vue 前端 (Vite dev server :5173) → 反向代理 `/api` → Spring Boot 后端 (:8080)
- 后端直连 MySQL，不经过 BFF 层
- JWT 由后端直接签发与校验（HMAC-SHA256）

## 2. 横切关注点：鉴权与用户上下文

当前约定的鉴权与上下文传递：

- 前端登录后获得 JWT token，存储于 Pinia store / localStorage
- Axios 拦截器在请求头添加 `Authorization: Bearer <token>`
- 后端 `JwtAuthenticationFilter` 解析 token，将 `UserContext` 写入 `UserContextHolder`（ThreadLocal）
- 应用层通过 `SecurityUtils` / `UserContextHolder` 获取当前用户上下文

约束：

- 后端自行校验 JWT（不在前端或网关层做）
- 应用层/领域层不得依赖 Web 框架对象（Request/Response），只能依赖 `UserContextHolder` 或方法参数传入的 `userId`
- Admin 端路由使用 `/admin/**` 路径，后端 `SecurityConfig` 限制为 `ROLE_ADMIN`

## 3. 标准分层

业务代码遵循四层结构（包路径 `com.consultation.*`）：

- `adapter`：对外适配层（HTTP Controller），只做协议转换与参数校验（`adapter/rest/`）
- `application`：用例编排层（AppService / Service），组合 domain 行为、调用 repository、管理事务边界
- `domain`：领域模型层（aggregate / vo / DomainService），承载业务不变量与状态机
- `infrastructure`：基础设施层（`infrastructure/persistence/`），提供 repository 实现、MyBatis-Plus mapper、PO 等

依赖方向（强制）：

- adapter → application → domain
- infrastructure → domain（实现 domain 定义的接口）
- domain 不得依赖 infrastructure / adapter / Web 框架

## 4. DTO、Assembler 与 Converter 约定

DTO 约定：

- 写操作入参使用 `*Command` 或 Request DTO（`adapter/rest/dto/`）
- 对外返回使用 `*Response`（`adapter/rest/dto/`）
- domain 与 infrastructure 之间使用 `*Converter` 做 PO ⟷ domain 转换
- application 层使用 `*Assembler` 做 domain ⟷ DTO 转换

映射约定：

- adapter 不直接操作 domain 实体
- 入参先在 Controller 中校验（`@Valid`），以 DTO 传入 application 层
- 使用手动转换（非 MapStruct），遵循现有 Converter/Assembler 命名

## 5. 持久化规则（MyBatis-Plus）

当前技术栈：

- MyBatis-Plus 3.5.5（Spring Boot 3 starter）
- PO（持久化对象）放在 `infrastructure/persistence/po/`
- Mapper 放在 `infrastructure/persistence/mapper/`
- Repository：domain 只定义 repository 接口（聚合根维度），infrastructure 提供实现（`*RepositoryImpl`）
- 主键使用 UUID 字符串（`VARCHAR(64)`），由应用层生成

聚合与一致性：

- 聚合内的子实体由 repository 在同一事务内完成装配与持久化
- 复杂查询优先走 Mapper + DTO，写操作必须通过聚合行为与 repository

## 6. 事务边界

事务边界统一放在 application 层：

- 以一个用例为单位在 AppService 方法上标注 `@Transactional`
- domain 方法不直接开启/提交事务
- `@Transactional` 不在 Controller 或 Repository 层使用

## 7. 错误模型与异常处理

错误处理统一使用 `ErrorResponse` 与全局异常处理：

- 业务错误：抛 `BusinessException`（携带 errorCode 与 message）
- 参数错误：抛 `IllegalArgumentException`，由 `GlobalExceptionHandler` 转为 HTTP 400
- 权限错误：由 Spring Security 返回 HTTP 401/403
- 资源冲突：HTTP 409

对外响应由 `GlobalExceptionHandler` 统一包装为 `ErrorResponse`：

```json
{
  "errorCode": "APPOINTMENT_CONFLICT",
  "message": "该时间段已被占用",
  "timestamp": "..."
}
```

## 8. 领域设计：聚合与规则

当前定义的聚合：

- `User`：用户（email + password + role + status）
- `Consultant`：咨询师（name + title + specialties + status）
- `Appointment`：预约（user + consultant + date + time + status）
- `Branch`：网点（name + address + phone + status）

业务规则引擎（`domain/businessrule/`）：

- `IBusinessRule` 接口：getCode() / getDescription() / getCategory()
- 规则以枚举实现 `IBusinessRule`（`AppointmentRule`、`AuthRule`）
- 规则配置 YAML 文件位于 `resources/business-rules/`
- `BusinessRuleController` 提供 GET 查询接口

数据字典（`domain/datadictionary/`）：

- `Description` 接口：枚举实现，提供 getCode() / getDescription()
- 5 个注册字典类型：USER_ROLE / USER_STATUS / APPOINTMENT_STATUS / CONSULTANT_STATUS / BRANCH_STATUS
- `DemoDataDictionaryType` 完成注册
- `DataDictionaryController` 提供 GET 查询接口

## 9. 预约状态机

```
                        ┌──────────┐
                  ┌────►│ CANCELLED │◄────┐
                  │     └──────────┘     │
  ┌─────────┐     │     ┌──────────┐     │
  │ PENDING │─────┼────►│CONFIRMED │────►│ COMPLETED
  └─────────┘     │     └──────────┘     │
                  │                      │
                  └──────────────────────┘
```

- PENDING：初始状态
- CONFIRMED：管理员确认
- COMPLETED：完成（终态）
- CANCELLED：从任意非终态取消
- 状态变更由 domain 层 `Appointment.canCancel()` 等守卫方法控制

## 10. 测试规范

### 测试技术栈

- JUnit 5 + SpringBootTest + RANDOM_PORT
- RestAssured 做 API 集成测试
- MariaDB4j 启动临时 MySQL 实例（替代 H2），确保 MySQL 方言兼容
- 测试使用 `schema.sql` + `data.sql` 初始化数据
- ResetDbService 在每个 `@BeforeEach` 中 TRUNCATE 全表

### 测试环境配置

- 测试使用 `application.yml`（test resources），配置 MariaDB4j
- Flyway 在测试中禁用（`spring.flyway.enabled=false`）
- `@ActiveProfiles("test")` 在 TestBase 中设置

### 测试覆盖要求

- 每个 API **必须** 有对应的集成测试
- 至少覆盖：
  - 1 个 Happy Path
  - 关键失败分支（参数错误 / 权限不足 / 业务冲突）
- DB 相关用例必须可重复执行，不依赖执行顺序
- 前端 Component/Composable 单元测试使用 Vitest（与源码同目录，`*.test.ts`）

### E2E 测试

- Playwright + Chromium，测试文件位于 `e2e/tests/`
- 运行命令：`bash e2e-run.sh`（启动前后端 + 等待就绪 + 执行测试）
- 每个 E2E 测试用例对应 `docs/qa/e2e-cases/test-cases.md` 中的文本用例

## 11. 前端规范

- Vue 3 Composition API + `<script setup>` 语法
- Pinia 管理全局状态（auth store 位于 `src/stores/auth.ts`）
- Vue Router 4 管理路由，布局文件位于 `src/layouts/`
- Axios 统一封装，请求拦截器自动添加 JWT，响应拦截器处理 401 跳转
- 全局 CSS 变量位于 `src/styles/variables.css`
- 视图文件按子系统分组：`src/views/user/`、`src/views/admin/`
- Service 层封装 API 调用（`src/services/xxxService.ts`），每个领域一个文件
- Composable 封装可复用逻辑（`src/composables/useXxx.ts`），如 `useBooking`、`useAuth`
- 页面级组件组合 Composable + UI 组件，视图内不含直接 API 调用

## 12. PR / 变更检查清单

- 分层检查：adapter 不含业务逻辑；domain 不依赖 infrastructure；事务边界在 application
- 错误处理：业务校验使用 `BusinessException`，避免裸 `RuntimeException`
- API 设计：RESTful 风格，路径统一 `/api/v1/resource`，admin 使用 `/admin/resource`
- 安全：不记录 token / 敏感信息；对外接口做鉴权检查
- 数据字典：新增枚举需要同时更新 `DemoDataDictionaryType` 与 `docs/dev/design-fact/others/data-dict.md`
- 业务规则：新增规则需要同时更新 YAML 配置与 `docs/dev/design-fact/others/businessrule.md`
- 数据库变更：新增 DDL 需同时更新 Flyway migration 文件、测试 `schema.sql`、以及 `docs/dev/design-fact/db.md`
- 验证：本地运行 `mvn test` 确认全部通过，`npm run build` 确认前端编译通过

## 13. 文档结构

文档体系位于 `docs/` 目录，所有 AI Agent 及开发人员必须遵守以下路径约定：

```
docs/
├── ba/               # 业务分析 (Business Analysis)
│   ├── chore/        # 会议转写材料
│   ├── feature/      # 需求原始规格
│   ├── prototype/    # 原型设计
│   └── business-fact/ # 业务事实
├── dev/              # 开发 (Development)
│   ├── standards/    # 开发规范（layers / db / api / security / naming / error / test / business-rule / data-dictionary）
│   ├── plans/        # 每轮任务的执行方案与变更记录
│   ├── adr/          # 架构决策记录 (Architecture Decision Records)
│   ├── design-fact/  # 系统当前设计现状（api.yaml / architecture.md / domain-model.md / error-code.md / security.md / state-machine.md / db.md）
│   └── snippet/      # 代码打样模版（DDD 各层常见写法参考）
└── qa/               # 质量保障 (Quality Assurance)
    ├── e2e-cases/    # E2E 测试用例
    ├── manual-cases/ # 手动执行用例
    └── smoking-cases/ # 快速回归用例
```

### 同步要求

- 每轮任务完成后，必须检查 `docs/dev/design-fact/` 中的内容是否因本次变更而需要更新
- 新增/修改 API → 更新 `docs/dev/design-fact/api.yaml`
- 新增/修改数据表 → 更新 `docs/dev/design-fact/db.md`
- 新增/修改业务规则 → 更新 `docs/dev/design-fact/others/businessrule.md`
- 新增/修改数据字典 → 更新 `docs/dev/design-fact/others/data-dict.md`

### 执行检查点

每轮任务提交描述中必须包含：

- [ ] `docs/` 同步检查（已更新 / 不涉及）

## 14. 开发流程（每轮需求的标准执行步骤）

每轮需求开发必须按以下顺序执行，严禁跳过或调换步骤：

1. **Plan 等待确认**：在 `docs/dev/plans/` 下根据模版创建 Plan，包含技术方案与任务拆分，等待人工确认后再继续。
2. **编写测试用例**：在 `docs/qa/e2e-cases/` 下编写本轮需求的 E2E 测试用例（文本格式，参照 `docs/qa/e2e-cases/test-cases.md`）。
3. **编写 API 测试**：根据 Plan 在 `backend/src/test/` 中编写 RestAssured 集成测试（API 级别），运行成功但断言失败（TDD 红牌阶段）。
4. **编写实现**：编写单元测试 + 实现代码（后端 application/domain/adapter 层），通过 `mvn compile` 编译。
5. **运行 API 测试**：运行 `mvn test` 修复失败的测试及其他错误，直至全部通过。
6. **浏览器调试**：使用 Playwright（`./e2e-run.sh`）启动前后端并调试，确认前端视觉效果和交互功能正常。如果报错使用 chrome-devtool-mcp 查看错误。
7. **编写 E2E 测试**：在 `e2e/tests/` 中编写 Playwright E2E 测试，覆盖本轮需求的所有测试场景，运行 `./e2e-run.sh` 全部通过。
