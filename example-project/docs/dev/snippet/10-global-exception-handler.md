# 全局异常处理（Infrastructure 层）

```java
package com.xxx.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadArg(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse("PARAM_ERROR", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleConflict(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("BUSINESS_CONFLICT", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        var msg = e.getBindingResult().getFieldError();
        return ResponseEntity.badRequest().body(
                new ErrorResponse("VALIDATION_ERROR",
                        msg != null ? msg.getDefaultMessage() : "参数错误"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("SYSTEM_ERROR", "系统繁忙"));
    }

    public record ErrorResponse(String errorCode, String errorMessage, Object data) {
        public ErrorResponse(String errorCode, String errorMessage) {
            this(errorCode, errorMessage, null);
        }
    }
}
```

## 异常 → HTTP 状态码映射

| 异常 | HTTP | errorCode |
|------|------|-----------|
| `IllegalArgumentException` | 400 | `PARAM_ERROR` |
| `MethodArgumentNotValidException` | 400 | `VALIDATION_ERROR` |
| `IllegalStateException` | 409 | `BUSINESS_CONFLICT` |
| `Exception`（未预期） | 500 | `SYSTEM_ERROR` |

## 要点

- 业务参数错误用 `IllegalArgumentException`，返回 400
- 业务冲突用 `IllegalStateException`，返回 409
- 未预期异常记录日志并返回 500，不暴露内部信息
- 响应体统一为 `ErrorResponse` 结构
