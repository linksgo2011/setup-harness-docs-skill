# 错误码规范

## 接口定义

所有异常实现 `IError` 接口：

```java
public interface IError {
    ErrorGroup getErrorGroup();
    String getSubCode();
    String getMessage();
    default String getErrorCode() { return getErrorGroup() + "_" + getSubCode(); }
}
```

## ErrorGroup 分组

| Group | 说明 | 默认 HTTP Status |
|-------|------|------------------|
| COMMON | 通用参数/资源错误 | 400/404 |
| AUTH | 认证/鉴权错误 | 401 |
| BUSINESS | 业务逻辑冲突 | 409 |
| SYSTEM | 系统内部错误 | 500 |

## 异常类层次

```
RuntimeException
 └── AbstractException (implements IError)
      ├── BusinessException      → HTTP 409
      ├── SystemException        → HTTP 500
      ├── ClientException        → HTTP 500
      └── UnauthorizedException  → HTTP 401
```

## 全局异常处理

- 使用 `@RestControllerAdvice` 统一拦截
- 按异常类型映射 HTTP 状态码
- 返回格式：`{errorCode, errorMessage, data}`

## 错误码表

全量错误码见 `docs/dev/design-fact/error-code.md`

---

## 前端错误处理

### Axios 响应拦截器

```typescript
api.interceptors.response.use(
  res => res,
  error => {
    const { status, data } = error.response || {}

    if (status === 401) {
      // Token 过期或无效 → 清除登录状态，跳转登录页
      localStorage.removeItem('token')
      window.location.href = '/login'
      return Promise.reject(error)
    }

    if (status === 403) {
      // 无权限 → 显示提示
      ElMessage.error('无权访问该资源')
      return Promise.reject(error)
    }

    if (status === 409) {
      // 业务冲突 → 将错误消息透传给调用方显示
      ElMessage.warning(data?.errorMessage || '操作冲突')
      return Promise.reject(error)
    }

    if (status >= 500) {
      ElMessage.error('系统繁忙，请稍后重试')
      return Promise.reject(error)
    }

    return Promise.reject(error)
  }
)
```

### 业务调用处处理

```typescript
try {
  await appointmentService.cancel(id, reason)
  ElMessage.success('取消成功')
  refreshList()
} catch (err: any) {
  // 409 已在拦截器中处理 toast
  // 400 错误展示具体提示
  if (err.response?.status === 400) {
    ElMessage.warning(err.response.data?.errorMessage || '参数错误')
  }
}
```

### 错误提示原则

| HTTP Status | 用户提示方式 | 说明 |
|-------------|-------------|------|
| 400 | `ElMessage.warning` | 参数错误，具体消息来自后端 |
| 401 | 自动跳转登录页 | Token 失效，静默处理 |
| 403 | `ElMessage.error` | 无权限，阻断操作 |
| 409 | `ElMessage.warning` | 业务冲突，具体消息来自后端 |
| 500 | `ElMessage.error` | 系统繁忙，通用提示 |
