# setup-harness-docs-skill

项目脚手架文档设置技能 — 用于为新项目快速搭建规范的文档体系。

## 结构

```
example-project/       ← 打磨中的示例项目，作为最佳实践参考
  ├── AGENTS.md        ← 完整的架构与工程规范
  ├── docs/            ← 分层文档体系（ba/dev/qa）
  ├── .agent/skills/   ← 开发流程 skills
  ├── backend/         ← Spring Boot 3 后端
  ├── frontend/        ← Vue 3 前端
  └── e2e/             ← Playwright E2E 测试

config files:          ← 根级模板文件
  ├── opencode.jsonc
  ├── .mcp.json
  └── .gitignore
```

## 使用方式

1. 参考 `example-project/docs/` 的目录结构为新项目建立文档体系
2. 参考 `example-project/AGENTS.md` 编写项目规范
3. 参考 `example-project/.agent/skills/` 创建项目 skills
4. 根级配置文件（opencode.jsonc、.mcp.json）可直接复制使用

## 设计原则

- `docs/ba/` — 业务分析（feature, business-fact, chore, prototype）
- `docs/dev/` — 开发（standards, plans, adr, design-fact）
- `docs/qa/` — 质量（e2e-cases, manual-cases, smoking-cases）
- 每层有明确边界，从需求到测试可追溯
