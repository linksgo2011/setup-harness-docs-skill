---
name: ba-review
description: 审核业务分析文档的完整性、一致性和对代码库的可追溯性。当被要求审核功能、业务事实或需求时使用。
---

# 业务分析文档审核

审核 `docs/ba/` 中的业务分析文档。

## 检查清单

- [ ] 功能规格（`docs/ba/feature/`）清晰描述了面向用户的行为
- [ ] 业务事实（`docs/ba/business-fact/`）与实际领域模型和领域服务一致
- [ ] 领域中的所有状态和迁移都已记录
- [ ] 文档中引用的业务规则与 `domain/businessrule/` 中的枚举和 YAML 配置匹配
- [ ] 种子数据与文档内容一致

## 交叉引用

| 文档 | 代码 |
|------|------|
| `docs/ba/feature/` | Controller、应用服务 |
| `docs/ba/business-fact/` | 领域聚合、业务规则、状态机 |
| `docs/ba/prototype/` | 前端视图和路由 |
