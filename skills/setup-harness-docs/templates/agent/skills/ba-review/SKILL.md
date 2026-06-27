---
name: ba-review
description: 审查业务分析文档的完整性、一致性和代码可追溯性。适用于审查需求规格、业务事实或需求的场景。
---

# BA 审查

审查 `docs/ba/` 中的业务分析文档。

## 检查清单

- [ ] 需求规格（`docs/ba/feature/`）清晰描述面向用户的行为
- [ ] 业务事实（`docs/ba/business-fact/`）与实际领域模型和领域服务一致
- [ ] 领域中的所有状态和转换都已记录
- [ ] 文档中引用的业务规则与 `domain/businessrule/` 枚举和 YAML 配置匹配
- [ ] 种子数据与文档描述一致

## 交叉引用

| 文档 | 代码 |
|------|------|
| `docs/ba/feature/` | 控制器、应用服务 |
| `docs/ba/business-fact/` | 领域聚合、业务规则、状态机 |
| `docs/ba/prototype/` | 前端视图和路由 |
