---
name: dev-bugfix
description: 通过复现、诊断和应用最小修复来修复 Bug。适用于用户报告 Bug 或测试失败的场景。
---

# 调试 Bug

## 流程

1. **复现** — 运行失败的测试或手动复现
2. **诊断** — 定位根因（查看日志、控制台、网络）
3. **修复** — 应用最小、精准的修复
4. **验证** — 确认修复解决问题且不破坏其他测试

## 本项目常见 Bug 来源

| 层 | 常见问题 |
|-------|---------------|
| Controller | 缺少 `@Valid`、错误的响应类型、路径不正确 |
| Application | 缺少 `@Transactional`、调用错误的领域方法 |
| Domain | 状态守卫过严/过松、缺少不变量检查 |
| Infrastructure | SQL 语法、MyBatis-Plus 映射错误、缺少索引 |
| Frontend | API 路径错误、缺少响应式更新、路由守卫不正确 |
| E2E | 选择器不匹配、时序问题、缺少 `waitFor` |

## 验证修复

```bash
cd backend && mvn test
./e2e-run.sh tests/app.spec.ts
```
