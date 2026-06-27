# 安全规范

## 后端

- 密码 BCrypt 加密，至少 8 位含字母+数字
- JWT HMAC-SHA256，24h 有效期，载荷含 userId/email/role
- 无状态鉴权，Token 传 `Authorization: Bearer <token>`
- 公开端点：`/auth/**`, `/home/**`, `GET /consultants`
- 管理端点：`/admin/**` 需 `ROLE_ADMIN`

## 前端

### Token 存储

```typescript
// 登录成功后
localStorage.setItem('token', response.token)
localStorage.setItem('user', JSON.stringify(response.user))
```

- Token 存入 `localStorage`，JWT 不涉及 XSS 时相对安全
- 用户信息同步存入 Pinia store：`authStore.setUser(user)`

### Axios 请求拦截器

```typescript
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})
```

### 路由守卫

```typescript
// src/router/index.ts
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || 'null')

  if (to.meta.requiresAuth && !token) return next('/login')
  if (to.meta.requiresAdmin && user?.role !== 'ROLE_ADMIN') return next('/')
  next()
})
```

### 登出

```typescript
localStorage.removeItem('token')
localStorage.removeItem('user')
authStore.clear()
router.push('/login')
```

### 安全注意事项

- 不在 localStorage 存储敏感信息（如 BCrypt 密码 hash）
- 后端返回的 JWT 不包含密码等敏感字段
- 前端不校验权限，仅做 UI 显示控制；权限校验由后端 `SecurityConfig` 强制执行
