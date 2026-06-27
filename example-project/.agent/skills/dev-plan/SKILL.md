---
name: dev-plan
description: 在编写任何代码之前在 docs/dev/plans/ 中创建开发计划。当开始新功能、重构或任何非简单变更时使用。
---

# 开发：制定计划

在实现前在 `docs/dev/plans/` 中创建计划。

## 模板

```markdown
# Plan: <标题>

## 目标

## 技术方案

## 任务拆分

- [ ] 任务 1
- [ ] 任务 2

## 需变更的文件

## 风险
```

## 规则

- 计划必须经用户审核确认后方可继续（AGENTS.md §14 第 1 步）
- 包含每个需要变更的文件的路径
- 参考现有模式（assembler、converter、service）以保持一致性
- 考虑文档同步：本次变更是否会影响 `docs/dev/design-fact/` 中的文件？
