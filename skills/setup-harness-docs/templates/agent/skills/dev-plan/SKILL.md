---
name: dev-plan
description: 在编写代码之前在 docs/dev/plans/ 中创建开发计划。适用于开始新功能、重构或任何非简单变更的场景。
---

# Dev 计划

在实现之前在 `docs/dev/plans/` 中创建计划。

## 模板

```markdown
# Plan: <title>

## Goal

## Technical Approach

## Task Breakdown

- [ ] Task 1
- [ ] Task 2

## Files to Change

## Risks
```

## 规则

- 计划必须经过用户审查确认后才能执行（AGENTS.md §14 步骤 1）
- 包含需要变更的每个文件的路径
- 参考现有模式（assembler、converter、service）以保持一致性
- 考虑文档同步：此次变更是否会影响 `docs/dev/design-fact/` 中的文件？
