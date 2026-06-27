# REST Controller（Adapter 层）

```java
package com.xxx.adapter.rest;

import com.xxx.application.service.XxxService;
import com.xxx.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/xxx")
@RequiredArgsConstructor
public class XxxController {

    private final XxxService xxxService;

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(xxxService.getMyXxx(SecurityUtils.currentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return ResponseEntity.ok(xxxService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateRequest r) {
        return ResponseEntity.ok(xxxService.create(SecurityUtils.currentUserId(), r.field1, r.field2));
    }

    @PutMapping("/{id}/action")
    public ResponseEntity<Void> action(@PathVariable String id, @RequestBody Map<String, String> body) {
        xxxService.action(id, SecurityUtils.currentUserId(), body.getOrDefault("reason", "默认原因"));
        return ResponseEntity.ok().build();
    }

    @Data
    public static class CreateRequest {
        @NotBlank String field1;
        String field2;
    }
}
```

## 要点

- Controller 只做协议转换，不包含业务逻辑
- 使用 `@Valid` 做参数校验，内联 DTO 类
- 通过 `SecurityUtils.currentUserId()` 获取当前用户
- 返回 `ResponseEntity<?>` 统一响应格式
