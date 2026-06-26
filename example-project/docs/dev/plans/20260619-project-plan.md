# 20260619 - Consultation Booking Platform 完整开发计划

## 总体策略

Domain 优先，逐层外扩。每个阶段围绕一个有边界的子域展开：

```
分析业务 → 设计 Domain → Infrastructure → Application → Adapter → Frontend
```

## 阶段划分

### Phase 0：基础骨架

**目标**：建立项目物理结构与基础设施基线

- 后端：pom.xml + 四层包结构 + 配置类（MyBatisPlus / Web / Jackson / Security / ExceptionHandler）+ JWT 基础设施 + BCrypt
- 前端：package.json + Vite + Vue Router + Pinia + Axios 拦截器 + 全局样式
- 文档：standards 目录完整（layers / db / api / naming / security）

### Phase 1：公共首页 + 用户认证

- Domain：User（email/password/name/role/status）
- 后端：Register + Login + JWT 签发 + Profile API
- 前端：首页 + 登录 + 注册 + AppHeader
- 数据表：user（含唯一索引 email）+ seed 管理员

### Phase 2：咨询师 + 预约管理

- Domain：Consultant + Appointment + TimeSlot
- 核心规则：预约冲突检测（唯一索引 + 代码校验）、状态机（PENDING→CONFIRMED→COMPLETED / CANCELLED）
- 后端：Consultant CRUD + Appointment CRUD + 时间段查询
- 前端：预约四步流程（选咨询师→选日期→选时间→确认）+ 我的预约列表
- 数据表：consultant + appointment + branch

### Phase 3：用户中心

- 用户 Dashboard 统计
- 个人信息编辑 + 密码修改

### Phase 4：管理后台

- 管理仪表盘
- 用户管理（启用/停用）
- 咨询师管理（CRUD）
- 预约管理
- 网点管理（CRUD）

## 依赖关系

```
Phase 0 (骨架)
  └─ Phase 1 (认证) ── 依赖 JWT + SecurityConfig
       └─ Phase 2 (预约) ── 依赖 Auth 上下文
            ├─ Phase 3 (用户中心)
            └─ Phase 4 (管理后台)
```

## 技术决策

- Java 17 + Spring Boot 3.2 + MyBatis-Plus 3.5.5
- JWT HMAC-SHA256 + BCrypt
- 主键 UUID VARCHAR(64)，公共字段 created_by/created_time/updated_by/updated_time
- 统一错误响应 `{errorCode, errorMessage, data}`
- API 前缀 `/api/v1/`，分页入参 page/pageSize
- 前端 Vue 3 + Composition API + Pinia + Vue Router 4 + Axios
- Vite 代理 `/api` → `localhost:8080`
