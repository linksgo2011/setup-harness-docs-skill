# 命名规范

## 后端

| 类型 | 约定 | 示例 |
|------|------|------|
| Controller | `XxxController` | `AppointmentController` |
| Service | `XxxService` | `AppointmentService` |
| Repository 接口 | `XxxRepository` | `AppointmentRepository` |
| Repository 实现 | `XxxRepositoryImpl` | `AppointmentRepositoryImpl` |
| Mapper | `XxxMapper` | `AppointmentMapper` |
| PO | `XxxPO` | `AppointmentPO` |
| Assembler | `XxxAssembler` | `AppointmentAssembler` |
| Converter | `XxxConverter` | `AppointmentConverter` |

## 前端

| 类型 | 约定 | 示例 |
|------|------|------|
| 组件文件 | PascalCase `.vue` | `BookingWizard.vue` |
| 视图文件 | PascalCase `.vue` | `UserDashboard.vue` |
| Service 文件 | camelCase `.ts` | `appointmentService.ts` |
| Store 文件 | camelCase `.ts` | `authStore.ts` |
| Composable | `useXxx` `.ts` | `useBooking.ts` |
| 变量/方法 | camelCase | `currentUser`, `fetchAppointments()` |
| 目录名 | kebab-case | `user-dashboard/` |
| 路由路径 | kebab-case | `/user/dashboard` |
| 组件名（多词） | PascalCase | `<BookingCalendar />` |
| Props 定义 | camelCase | `:userName="..."` |
| Events | kebab-case | `@update-profile` |

## 数据库

- 小写蛇形，单数表名：`appointment`、`consultant`
- 字段小写蛇形：`appointment_date`、`start_time`
- 唯一索引前缀 `uk_`：`uk_appointment_slot`
- 普通索引前缀 `idx_`：`idx_user_id`
