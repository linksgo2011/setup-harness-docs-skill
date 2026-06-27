# Converter（Infrastructure 层 — PO ⟷ Domain 转换）

```java
package com.xxx.infrastructure.persistence.converter;

import com.xxx.domain.aggregate.Xxx;
import com.xxx.domain.vo.XxxStatus;
import com.xxx.infrastructure.persistence.po.XxxPO;
import org.springframework.stereotype.Component;

@Component
public class XxxConverter {

    public Xxx toDomain(XxxPO po) {
        if (po == null) return null;
        return Xxx.builder()
                .id(po.getId())
                .userId(po.getUserId())
                .field1(po.getField1())
                .field2(po.getField2())
                .date(po.getDate())
                .status(XxxStatus.valueOf(po.getStatus()))
                .note(po.getNote())
                .build();
    }

    public XxxPO toPO(Xxx xxx) {
        if (xxx == null) return null;
        XxxPO po = new XxxPO();
        po.setId(xxx.getId());
        po.setUserId(xxx.getUserId());
        po.setField1(xxx.getField1());
        po.setField2(xxx.getField2());
        po.setDate(xxx.getDate());
        po.setStatus(xxx.getStatus().name());
        po.setNote(xxx.getNote());
        return po;
    }
}
```

## 要点

- `@Component`，两层双向转换
- `toDomain` 使用 Domain 的 `@Builder` 构建
- `toPO` 使用 PO 的 setter（Lombok `@Data`）
- 枚举字段通过 `.name()` / `valueOf()` 与字符串互转
