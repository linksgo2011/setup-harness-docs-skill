# ADR-002: 主键采用 UUID 字符串而非自增整数

- **状态**: 已接受
- **日期**: 2026-06-19

## 上下文

四张核心表（user、consultant、appointment、branch）需要主键策略。选择包括自增整数、UUID 二进制、UUID 字符串。

## 决策

所有表主键采用 UUID 字符串（`VARCHAR(64)`），由应用层 Java 代码生成。

## 备选方案

| 方案 | 理由 | 否决原因 |
|------|------|----------|
| BIGINT AUTO_INCREMENT | 简单、高性能 | 暴露记录数、合并数据库时有冲突风险 |
| UUID BINARY(16) | 存储紧凑 | MyBatis-Plus 处理不方便，调试时不可读 |
| UUID VARCHAR(64) | 可读、无冲突、不暴露信息 | 存储略大（MySQL InnoDB 可接受） |

## 后果

**正面**：
- 主键不暴露记录数，安全
- 跨数据库迁移无冲突
- 日志中可直接查看和关联 ID

**负面**：
- VARCHAR(64) PK 在 InnoDB 聚簇索引中比 BIGINT 占用更多空间
- 需在应用层手动生成 UUID（非数据库自动生成）
