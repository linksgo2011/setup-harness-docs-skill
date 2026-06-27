# setup-harness-docs-skill

项目脚手架文档设置技能 — 用于为新项目或现有项目快速搭建规范的文档体系。

## 快速开始

```bash
bash skills/setup-harness-docs/scaffold.sh <project-name> <backend-type> <frontend-type>

# 示例：创建带有完整文档结构的 Spring Boot + Vue 项目
bash skills/setup-harness-docs/scaffold.sh my-app spring-boot vue
```

创建如下结构：

```
my-app/
├── docs/                  # 文档层次结构
│   ├── ba/                #   业务分析
│   ├── dev/                #   开发
│   │   └── snippet/        #     代码模式模板
│   └── qa/                #   质量保障
├── .agent/skills/         # 8 个开发流程 skills
├── AGENTS.md              # 架构与工程规范
├── opencode.jsonc         # opencode 配置
├── .mcp.json              # MCP 配置 (chrome-devtools)
└── .gitignore
```

## 项目结构

```
skills/setup-harness-docs/  ← 本 skill（自动初始化文档体系）
  ├── SKILL.md
  ├── scaffold.sh
  └── templates/

example-project/                   ← 最佳实践参考示例
  ├── AGENTS.md
  ├── docs/
  ├── .agent/skills/
  ├── backend/                     ← Spring Boot 3 单体后端
  ├── frontend/                    ← Vue 3 前端
  └── e2e/                         ← Playwright E2E 测试

配置文件：
  ├── opencode.jsonc
  ├── .mcp.json
  └── .gitignore
```

## 设计原则

- `docs/ba/` — 业务分析（feature, business-fact, chore, prototype）
- `docs/dev/` — 开发（standards, plans, adr, design-fact, snippet）
- `docs/qa/` — 质量（e2e-cases, manual-cases, smoking-cases）
- 每层有明确边界，从需求到测试可追溯
