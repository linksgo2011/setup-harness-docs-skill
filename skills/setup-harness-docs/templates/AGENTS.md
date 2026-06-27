# {{PROJECT_NAME}} — 架构与工程规范

本文件定义本项目架构边界、分层规则、模块职责与工程规范。

## 技术栈

- 后端：{{BACKEND_TYPE}}
- 前端：{{FRONTEND_TYPE}}

## 标准目录结构

```
{{PROJECT_NAME}}/
├── docs/             ← 文档（ba/dev/qa）
├── .agent/skills/    ← 开发流程 skills
├── AGENTS.md         ← 本文件
├── opencode.jsonc
├── .mcp.json
└── .gitignore
```

*根据实际技术栈补充：backend/、frontend/、e2e/ 等模块目录。*

## 文档体系

```
docs/
├── ba/               # 业务分析
│   ├── chore/
│   ├── feature/
│   ├── prototype/
│   └── business-fact/
├── dev/              # 开发
│   ├── standards/
│   ├── plans/
│   ├── adr/
│   ├── design-fact/
│   └── snippet/
└── qa/               # 质量
    ├── e2e-cases/
    ├── manual-cases/
    └── smoking-cases/
```

### 同步要求

- 每轮任务完成后必须检查 `docs/dev/design-fact/` 是否需要同步更新
- 新增/修改 API → 更新 design-fact/api.yaml
- 新增/修改数据表 → 更新 design-fact/db.md
- 新增/修改业务规则 → 更新 design-fact/others/businessrule.md
- 新增/修改数据字典 → 更新 design-fact/others/data-dict.md

## 开发流程

每轮需求开发按以下步骤执行：

1. **Plan 等待确认**：在 `docs/dev/plans/` 创建方案
2. **编写测试用例**：在 `docs/qa/e2e-cases/` 编写文本用例
3. **编写 API 测试**：写集成测试（TDD 红牌）
4. **编写实现**：实现代码
5. **运行测试**：确保所有测试通过
6. **浏览器调试**：UI 交互验证
7. **编写 E2E 测试**：Playwright 自动化

## 全栈开发规范

每条规范同时覆盖后端与前端。参考 `example-project/` 中的具体内容填充。

| 规范 | 后端 | 前端 |
|------|------|------|
| 分层架构 | DDD 四层（adapter/application/domain/infrastructure） | View → Composable → Service → Store |
| API 调用 | RESTful `/api/v1/` | Axios 实例 + 拦截器 |
| 安全 | JWT + Spring Security | Token localStorage + 路由守卫 |
| 错误处理 | GlobalExceptionHandler + IError | Axios 响应拦截器 + ElMessage |
| 测试 | JUnit 5 + RestAssured | Playwright E2E + Vitest |

## 开发规范

*在此处补充具体的开发规范，参考 example-project/AGENTS.md。*
