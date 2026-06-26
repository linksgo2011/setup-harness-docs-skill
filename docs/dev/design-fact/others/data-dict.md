# 数据字典清单（事实方案）

> 所有枚举类型均实现 `Description` 接口，可通过 `GET /api/v1/data-dictionaries/{type}` 动态查询。

## UserRole — 用户角色

| Code | 描述 | 说明 |
|------|------|------|
| ROLE_USER | 普通用户 | 默认角色，可预约咨询 |
| ROLE_ADMIN | 管理员 | 可访问管理后台 |

## UserStatus — 用户状态

| Code | 描述 | 说明 |
|------|------|------|
| ACTIVE | 正常 | 可用 |
| DISABLED | 已停用 | 不可登录和操作 |

## AppointmentStatus — 预约状态

| Code | 描述 | 业务含义 |
|------|------|----------|
| PENDING | 待确认 | 初始状态，等待咨询师确认 |
| CONFIRMED | 已确认 | 咨询师已确认，预约生效 |
| COMPLETED | 已完成 | 咨询已结束，终态 |
| CANCELLED | 已取消 | 用户或管理员取消，终态 |

**状态机流转**：

```
PENDING ──→ CONFIRMED ──→ COMPLETED
   │            │
   └── CANCELLED ←──┘
```

## ConsultantStatus — 咨询师状态

| Code | 描述 | 说明 |
|------|------|------|
| ACTIVE | 可预约 | 可在预约列表展示和选择 |
| DISABLED | 不可预约 | 不展示在预约列表 |

## BranchStatus — 网点状态

| Code | 描述 | 说明 |
|------|------|------|
| ACTIVE | 营业中 | 正常运营 |
| DISABLED | 已关闭 | 停止运营 |

## DataDictionaryType — 字典类型注册表

| Code | 关联枚举 | 描述 | 查询端点 |
|------|----------|------|----------|
| USER_ROLE | UserRole | 用户角色字典 | `/api/v1/data-dictionaries/USER_ROLE` |
| USER_STATUS | UserStatus | 用户状态字典 | `/api/v1/data-dictionaries/USER_STATUS` |
| APPOINTMENT_STATUS | AppointmentStatus | 预约状态字典 | `/api/v1/data-dictionaries/APPOINTMENT_STATUS` |
| CONSULTANT_STATUS | Consultant.ConsultantStatus | 咨询师状态字典 | `/api/v1/data-dictionaries/CONSULTANT_STATUS` |
| BRANCH_STATUS | Branch.BranchStatus | 网点状态字典 | `/api/v1/data-dictionaries/BRANCH_STATUS` |

## 同步策略

修改枚举后，运行 `DataDictionaryUtil.generateMarkdown()` 更新本文档。
