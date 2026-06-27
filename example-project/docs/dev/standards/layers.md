# 分层架构规范

## 四层架构

```
adapter → application → domain ← infrastructure
```

## adapter
HTTP Controller 入/出参协议转换、参数基础校验。不含业务逻辑。

## application
用例编排、事务边界管理、Command→domain→Response 转换（Assembler）。

## domain
核心业务逻辑、Entity/Aggregate/ValueObject、Repository 接口定义。不依赖 infrastructure。

## infrastructure
PO + Mapper + Converter + RepositoryImpl + 安全 + 配置。

---

## 前端（Vue 3）分层

```
view (页面路由) → component (UI 组件) → composable (可复用逻辑) → service (API 调用)
                                                                       ↓
                                                                 store (Pinia 全局状态)
```

### view
页面级组件，对应 Vue Router 路由。负责布局与页面组合，不含业务逻辑。

### component
通用 UI 组件（表单、表格、卡片等），接收 props、发射 events。

### composable
可复用的组合式逻辑（`useAuth`、`useBooking` 等），封装响应式状态与方法。

### service
Axios API 调用封装，每个领域一个 service 文件（`authService.ts`、`appointmentService.ts`）。

### store（Pinia）
全局状态管理（`authStore`、`bookingStore`），使用 Composition API 风格定义。
