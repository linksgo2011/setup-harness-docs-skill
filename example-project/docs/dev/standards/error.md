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
