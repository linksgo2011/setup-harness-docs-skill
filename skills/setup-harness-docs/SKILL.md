---
name: setup-harness-docs
description: Initialize a project with the harness documentation structure (docs/ba/dev/qa), AGENTS.md conventions, opencode config, and development skills. Use when asked to set up docs, initialize a project, or scaffold a new codebase.
---

# setup-harness-docs

为新项目或存量项目初始化规范的文档体系。参考 `example-project/` 的最佳实践生成模版。

## 快速开始

```bash
# 在目标项目根目录运行
bash /path/to/setup-harness-docs-skill/.agent/skills/setup-harness-docs/scaffold.sh <project-name> <backend-type> <frontend-type>

# 示例
bash ../setup-harness-docs-skill/.agent/skills/setup-harness-docs/scaffold.sh my-app spring-boot vue
```

## 生成内容

```
<project-name>/
├── docs/                     ← 分层文档体系
│   ├── ba/                   │   业务分析
│   │   ├── chore/             │   会议转写材料
│   │   ├── feature/           │   需求规格
│   │   ├── prototype/         │   原型设计
│   │   └── business-fact/     │   业务事实
│   ├── dev/                  │   开发
│   │   ├── standards/         │   开发规范
│   │   ├── plans/             │   执行方案
│   │   ├── adr/               │   架构决策
│   │   └── design-fact/       │   设计事实
│   └── qa/                   │   质量
│       ├── e2e-cases/         │   E2E 用例
│       ├── manual-cases/      │   手动用例
│       └── smoking-cases/     │   回归用例
├── .agent/skills/            ← 开发流程 skills（8 个）
├── AGENTS.md                 ← 工程规范
├── opencode.jsonc            ← opencode 配置
├── .mcp.json                 ← MCP 配置（chrome-devtools）
└── .gitignore                ← 忽略规则
```

## 使用场景

| 场景 | 做法 |
|------|------|
| **新项目** | 直接运行 scaffold.sh，然后按 AGENTS.md 完善内容 |
| **存量项目** | 同上，docs/ 已有内容则手工合并到对应目录 |
| **参考示例** | 查看 `example-project/` 中的实际填充内容作为参考 |

## 步骤

1. 运行 scaffold.sh 初始化文档目录结构和配置
2. 参考 `example-project/AGENTS.md` 编写项目专用的 AGENTS.md
3. 在 `docs/ba/feature/` 下编写需求规格
4. 在 `docs/dev/standards/` 下编写开发规范
5. 在 `docs/ba/business-fact/` 下根据领域模型编写业务事实
6. 在 `docs/dev/plans/` 下编写开发计划
7. 在 `docs/qa/` 下编写测试用例

## 参考

- `example-project/` — 完整的最佳实践示例
- `example-project/AGENTS.md` — 架构与工程规范模版
- `example-project/.agent/skills/` — 8 个开发流程 skills
- `example-project/docs/` — 完整的文档填充范例
