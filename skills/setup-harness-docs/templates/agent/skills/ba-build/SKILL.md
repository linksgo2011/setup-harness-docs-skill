---
name: ba-build
description: 根据代码和需求创建或更新业务分析文档。适用于编写需求规格、业务事实或领域知识的场景。
---

# BA 构建

根据代码库编写业务分析文档。

## 信息来源

| 文档 | 来源 |
|-----|--------|
| `docs/ba/feature/<name>.md` | 需求请求、用户故事、领域聚合、控制器 |
| `docs/ba/business-fact/booking-flow.md` | 领域服务、状态机、业务规则、种子数据 |
| `docs/ba/business-fact/domain-model.md` | 聚合、值对象、仓储、领域服务 |
| `docs/ba/chore/` | 会议纪要（手动输入，无法逆向还原） |
| `docs/ba/prototype/` | 前端视图、路由、UI 流程（手动输入或截图） |

## 约定

- 面向业务的文档使用中文编写
- 实体字段、枚举值和状态转换使用表格呈现
- 引用具体的代码路径以保持文档可追溯
