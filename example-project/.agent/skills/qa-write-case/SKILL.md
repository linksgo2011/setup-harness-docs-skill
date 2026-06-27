---
name: qa-write-case
description: 编写手工和冒烟测试用例文档。当被要求为功能创建测试文档时使用。
---

# 质量：编写测试用例

在编写代码之前编写测试用例文档（AGENTS.md §14 第 2 步）。

## 位置

| 类型 | 路径 |
|------|------|
| E2E 测试用例（文本） | `docs/qa/e2e-cases/` |
| 手工测试用例 | `docs/qa/manual-cases/manual-test-cases.md` |
| 冒烟测试用例 | `docs/qa/smoking-cases/smoking-test-cases.md` |

## E2E 用例模板

```markdown
### TC-<id>: <标题>

| 字段 | 值 |
|------|-----|
| 前置条件 | |
| 步骤 | |
| 预期结果 | |
```

## 手工用例模板

```markdown
### TC-M-<id>: <标题>

| 项目 | 内容 |
|------|------|
| 前置条件 | |
| 步骤 | 1. ... |
| 预期结果 | - ... |
```

## 规则

- 覆盖：正常路径、错误情况、边界情况
- 使用种子数据（admin@test.com、3 位咨询师、3 个网点）
- `docs/ba/feature/` 中的每个功能规格应有对应的测试用例
