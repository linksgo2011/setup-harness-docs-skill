---
name: qa-write-case
description: 编写手动测试和回归测试用例文档。适用于为功能创建测试文档的场景。
---

# QA 编写用例

在编写代码之前编写测试用例文档（AGENTS.md §14 步骤 2）。

## 存放位置

| 类型 | 路径 |
|------|------|
| E2E 测试用例（文本） | `docs/qa/e2e-cases/` |
| 手动测试用例 | `docs/qa/manual-cases/manual-test-cases.md` |
| 回归测试用例 | `docs/qa/smoking-cases/smoking-test-cases.md` |

## E2E 用例模板

```markdown
### TC-<id>: <title>

| Field | Value |
|-------|-------|
| Precondition | |
| Steps | |
| Expected | |
```

## 手动用例模板

```markdown
### TC-M-<id>: <title>

| Project | Content |
|---------|---------|
| Precondition | |
| Steps | 1. ... |
| Expected | - ... |
```

## 规则

- 覆盖：正常路径、错误场景、边界情况
- 使用种子数据（admin@test.com, 3 位咨询师, 3 个分支机构）
- `docs/ba/feature/` 中的每个需求规格应有对应的测试用例
