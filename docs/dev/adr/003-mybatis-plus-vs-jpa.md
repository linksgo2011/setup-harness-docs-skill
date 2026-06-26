# ADR-003: 持久化框架选择 MyBatis-Plus 而非 JPA/Hibernate

- **状态**: 已接受
- **日期**: 2026-06-19

## 上下文

项目中需要 ORM 框架管理数据库映射。候选方案包括 Spring Data JPA（Hibernate）和 MyBatis-Plus。

## 决策

采用 MyBatis-Plus 3.5.5（Spring Boot 3 Starter）。

## 备选方案

| 方案 | 理由 | 否决原因 |
|------|------|----------|
| Spring Data JPA | 对象映射自动、HQL 灵活 | N+1 问题排查困难、复杂查询难优化、DDD 聚合装配不直接 |
| MyBatis-Plus | SQL 可控、映射直接、分页插件成熟 | 需手写部分 XML/注解 SQL |

## 后果

**正面**：
- SQL 完全可控，可针对 MySQL 优化
- MyBatis-Plus `BaseMapper` 提供基础 CRUD 无需写 SQL
- 分页查询简单（`PaginationInnerInterceptor`）

**负面**：
- 关联查询需手写 SQL（如统计类查询）
- 无 Hibernate 的自动脏检查（需显式调用 update）
