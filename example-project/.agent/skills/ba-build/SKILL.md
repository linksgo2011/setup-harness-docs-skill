---
name: ba-build
description: 从代码和需求创建或更新业务分析文档。当被要求记录功能、业务事实或领域知识时使用。
---

# 业务分析文档构建

基于代码库编写业务分析文档。

## 来源

| 文档 | 来源 |
|------|------|
| `docs/ba/feature/<name>.md` | 功能需求、用户故事、领域聚合、Controller |
| `docs/ba/business-fact/booking-flow.md` | 领域服务、状态机、业务规则、种子数据 |
| `docs/ba/business-fact/domain-model.md` | 聚合、值对象、Repository、领域服务 |
| `docs/ba/chore/` | 会议记录（手动输入，无法反向工程） |
| `docs/ba/prototype/` | 前端视图、路由、UI 流程（手动输入或截图） |

## 约定

- 面向业务的文档使用中文编写
- 包含实体字段、枚举值和状态迁移的表格
- 引用具体的代码路径以保持文档可追溯
