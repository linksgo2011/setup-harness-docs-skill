# 状态机设计

> 本文档描述 AppointmentStatus 的完整状态机事实（as-built）。

## 状态定义

| Code | 描述 | 含义 |
|------|------|------|
| PENDING | 待确认 | 初始状态，用户已提交但等待确认 |
| CONFIRMED | 已确认 | 咨询师/管理员已确认，预约生效 |
| COMPLETED | 已完成 | 咨询已结束，**终态** |
| CANCELLED | 已取消 | 用户或管理员取消，**终态** |

## 状态流转图

```
        ┌──────────┐
        │  PENDING │ ← 创建预约后初始状态
        └────┬─────┘
             │
      ┌──────┴──────┐
      │             │
      ▼             ▼
┌──────────┐  ┌──────────┐
│CONFIRMED │  │CANCELLED │ ← 用户可取消
└────┬─────┘  └──────────┘
     │
     ▼
┌──────────┐
│COMPLETED │
└──────────┘
```

## 转换表

| 当前状态 | 目标状态 | 触发操作 | 守卫条件 | 代码位置 |
|----------|----------|----------|----------|----------|
| (新创建) | PENDING | 创建预约 (AppointmentService.create) | 咨询师可预约 + 无冲突 | `AppointmentService.java:50-53` |
| PENDING | CONFIRMED | 管理员确认 (预留) | - | 未实现 |
| PENDING | CANCELLED | 取消预约 (AppointmentService.cancel) | `!COMPLETED && !CANCELLED` | `Appointment.java` `canCancel()` |
| CONFIRMED | COMPLETED | 管理员完成 (预留) | - | 未实现 |
| CONFIRMED | CANCELLED | 取消预约 (AppointmentService.cancel) | `!COMPLETED && !CANCELLED` | `Appointment.java` `canCancel()` |

## 守卫条件

```
canCancel() = status != COMPLETED && status != CANCELLED
```

- PENDING 可取消
- CONFIRMED 可取消
- COMPLETED **不可取消**
- CANCELLED **不可再次取消**

## 业务规则集成

**冲突检测**（AppointmentConflictService）：
- 创建预约时，排除 status == CANCELLED 的旧预约
- 允许在同一咨询师同一时段重新预约（如果之前的预约已取消）
- 由 `uk_appointment_slot` 唯一索引 + 代码双重校验

## 待实现状态转换

| 动作 | 优先级 | 说明 |
|------|--------|------|
| PENDING → CONFIRMED | 低 | 目前无确认端点，需管理后台功能 |
| CONFIRMED → COMPLETED | 低 | 同上 |
