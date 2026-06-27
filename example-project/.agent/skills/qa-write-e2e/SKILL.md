---
name: qa-write-e2e
description: 编写并运行自动化测试——后端 API 集成测试（RestAssured）和前端 E2E 测试（Playwright）。当被要求编写或运行测试时使用。
---

# 质量：编写 E2E 测试

## 后端 API 测试（RestAssured）

```bash
cd backend && mvn test
```

测试类位于 `backend/src/test/java/com/consultation/adapter/rest/`：
- `AuthControllerTest` — 注册、登录、重复邮箱、错误密码
- `AppointmentControllerTest` — 创建、列表、冲突、取消
- `AdminControllerTest` — 仪表盘、用户、网点 CRUD、数据字典、业务规则
- `ConsultantControllerTest` — 列表、时段、无效咨询师
- `UserControllerTest` — 个人资料、更新、修改密码

## 模式

```java
// TDD 红牌阶段：先写测试，再实现
// TestBase 提供：loginAsUser()、loginAsAdmin()、authHeader()
@Test
void should_create_appointment() {
    String token = loginAsUser();
    // ... RestAssured.given().header(...).post(...)
}
```

## Playwright E2E 测试

```bash
./e2e-run.sh
# 或指定文件：
./e2e-run.sh tests/app.spec.ts
```

`e2e/tests/app.spec.ts` 中的测试覆盖：公共页面、认证、用户生命周期、管理流程、边界情况。

## 编写新测试

1. 在 `e2e/tests/app.spec.ts` 的已有 describe 块中添加测试或创建新块
2. 遵循现有模式（先登录、交互、断言）
3. 运行 `./e2e-run.sh tests/app.spec.ts` 验证
