# ADR-001: 采用四层 DDD 单体架构

- **状态**: 已接受
- **日期**: 2026-06-19

## 上下文

项目需构建一个全栈咨询预约平台，后端需要支撑：
- 清晰的业务逻辑与基础设施分离
- 业务规则与状态机的可维护性
- 适应未来的微服务拆分

## 决策

采用四层 DDD（Domain-Driven Design）单体架构：

```
adapter（HTTP Controller）→ application（Service）
    → domain（Aggregate / VO / DomainService）
    ← infrastructure（Persistence / Security）
```

依赖规则：
- 依赖方向单向：adapter → application → domain；infrastructure → domain
- domain 层零外部依赖（不依赖 Spring、Web、数据库框架）

## 备选方案

| 方案 | 理由 | 否决原因 |
|------|------|----------|
| 传统三层（Controller/Service/DAO） | 简单直接 | 业务逻辑易泄漏到 Service 层，领域规则分散 |
| 纯微服务 | 独立部署、扩展 | 团队规模小，过早拆分增加运维复杂度 |

## 后果

**正面**：
- 领域逻辑集中在 domain 层，便于测试与推理
- 基础设施可替换（如 MySQL → PostgreSQL 只改 infrastructure 层）
- 为未来拆分保留清晰边界

**负面**：
- 初期开发速度略慢于三层（需维护 converter/assembler）
- 简单 CRUD 需经过四层，代码量较大

## 遵循度

- 新代码必须遵循四层依赖方向
- CI 应通过 `archunit` 或代码审查确保无反向依赖
