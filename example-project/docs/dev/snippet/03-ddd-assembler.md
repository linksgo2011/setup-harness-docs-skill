# Assembler（Domain → DTO 转换）

```java
package com.xxx.application.assembler;

import com.xxx.domain.aggregate.Xxx;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class XxxAssembler {

    public XxxResponse toResponse(Xxx xxx) {
        if (xxx == null) return null;
        return XxxResponse.builder()
                .id(xxx.getId())
                .userId(xxx.getUserId())
                .field1(xxx.getField1())
                .field2(xxx.getField2())
                .status(xxx.getStatus().name())
                .build();
    }

    @Data @Builder
    public static class XxxResponse {
        String id;
        String userId;
        String field1;
        String field2;
        String status;
    }
}
```

## 要点

- Assembler 负责 Domain → DTO 的转换，在 Application 层
- Converter（Infrastructure 层）负责 Domain ⟷ PO 的转换 — 见 `08-ddd-converter.md`
- DTO 使用 `@Data @Builder`，内联为静态类
