# 系统架构设计

> 本文档描述咨询预约平台的事实架构（as-built）。

## 部署形态

```
┌─────────────────────────────────────────────────────────┐
│                    单机部署 (Monolith)                    │
│  ┌──────────┐    ┌──────────────────────────────────┐   │
│  │          │    │           Spring Boot 3.2          │   │
│  │  Vite    │ /api│  ┌────────────────────────────┐   │   │
│  │  Dev     │─────│  │       adapter/rest          │   │   │
│  │  Server  │     │  │  (Controller, 协议转换)      │   │   │
│  │  :5173   │     │  └──────────┬─────────────────┘   │   │
│  │          │     │             ↓                      │   │
│  │  or      │     │  ┌────────────────────────────┐   │   │
│  │  Nginx   │     │  │     application/service     │   │   │
│  │  :80     │     │  │  (用例编排, 事务边界)        │   │   │
│  └──────────┘     │  └──────────┬─────────────────┘   │   │
│                   │             ↓                      │   │
│                   │  ┌────────────────────────────┐   │   │
│                   │  │     domain/aggregate        │   │   │
│                   │  │  (业务不变量, 状态机)        │   │   │
│                   │  └──────────┬─────────────────┘   │   │
│                   │             ↑                      │   │
│                   │  ┌──────────┴─────────────────┐   │   │
│                   │  │  infrastructure/            │   │   │
│                   │  │  (DB/外部API/安全/配置)      │   │   │
│                   │  └────────────────────────────┘   │   │
│                   │              │                    │   │
│                   │              ↓                    │   │
│                   │       ┌────────────┐              │   │
│                   │       │   MySQL    │              │   │
│                   │       │   :3306    │              │   │
│                   │       └────────────┘              │   │
│                   └──────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

## 通信方式

| 端 | 目标 | 协议 | 说明 |
|----|------|------|------|
| 浏览器 → Vite Dev Server | 前端静态资源 | HTTP | 开发模式，Vite 热更新 |
| Vite Dev Server → Spring Boot | 后端 API | HTTP (proxy) | `/api` → `localhost:8080`，由 `vite.config.ts` 配置 |
| Spring Boot → MySQL | 数据库 | JDBC | MyBatis-Plus 连接池，默认 3306 |
| Nginx (生产) → 静态文件 | 前端 | HTTP | 打包后的 dist/ |
| Nginx (生产) → Spring Boot | 后端 API | HTTP | 反向代理 `/api` |

## 四层依赖方向

```
adapter → application → domain ← infrastructure
```

- **adapter**：仅做 HTTP 协议转换与参数校验，不包含业务逻辑
- **application**：用例编排、事务管理、DTO ↔ Domain 转换（Assembler）
- **domain**：纯 POJO + Lombok，无 Spring 注解，承载业务不变量
- **infrastructure**：实现 domain 的 Repository 接口 + 安全 + 配置

## 前端架构（Vue 3）

```
src/
├── views/                    # 页面级组件（按子系统分）
│   ├── user/                 #   用户端（dashboard, book, appointments, profile）
│   └── admin/                #   管理端（dashboard, users, consultants, appointments, branches）
├── components/               # 通用 UI 组件
│   ├── layout/               #   布局组件（AppHeader, AppSidebar, AppFooter）
│   └── common/               #   通用组件（StatCard, BookingCalendar, DataTable）
├── composables/              # 可复用组合式逻辑
│   ├── useAuth.ts            #   登录/注册/登出逻辑
│   ├── useBooking.ts         #   预约流程状态管理
│   └── usePagination.ts      #   分页逻辑
├── services/                 # API 服务层
│   ├── axios.ts              #   Axios 实例（拦截器配置）
│   ├── authService.ts        #   认证相关 API
│   ├── appointmentService.ts #   预约相关 API
│   └── consultantService.ts  #   咨询师相关 API
├── stores/                   # Pinia 全局状态
│   ├── authStore.ts          #   用户认证状态
│   └── bookingStore.ts       #   预约流程临时状态
├── router/                   # Vue Router 配置
│   └── index.ts              #   路由定义 + 守卫
├── styles/                   # 全局样式
│   └── variables.css         #   CSS 变量
└── types/                    # TypeScript 类型定义
    └── index.ts              #   接口 DTO 类型
```

## 技术栈

| 组件 | 技术 | 版本 |
|------|------|------|
| JDK | OpenJDK | 17 |
| 框架 | Spring Boot | 3.2.0 |
| ORM | MyBatis-Plus | 3.5.5 |
| DB | MySQL 8.0 / H2 (test) | - |
| Migration | Flyway | (Boot managed) |
| 安全 | Spring Security + JWT (jjwt) | 0.12.3 |
| 前端框架 | Vue 3 + Composition API + TypeScript | - |
| 构建 | Vite | 5.x |
| 状态管理 | Pinia | - |
| 路由 | Vue Router 4 | - |
| 测试 | JUnit 5 + RestAssured | - |

## 包结构

```
com.consultation
├── ConsultationApplication.java
├── adapter.rest.*             # Controller
├── application.assembler.*    # Response/Command DTO
├── application.service.*      # AppService
├── domain.aggregate.*         # 聚合根
├── domain.repository.*        # 仓储接口
├── domain.service.*           # 领域服务
├── domain.vo.*                # 值对象
├── domain.businessrule.*      # 业务规则接口 (Phase 2)
├── domain.datadictionary.*    # 数据字典接口 (Phase 3)
└── infrastructure.*           # 配置/持久化/安全/规则实现
```
