# Domain Service（Domain 层）

```java
package com.xxx.domain.service;

import com.xxx.domain.aggregate.Xxx;
import com.xxx.domain.vo.XxxStatus;
import java.util.List;

public class XxxConflictService {

    /**
     * 校验新建记录与已有记录不冲突。
     * 冲突时抛出 IllegalStateException（HTTP 409）。
     */
    public void validateNoConflict(List<Xxx> existing, CheckParam param) {
        boolean conflict = existing.stream().anyMatch(a ->
                a.getField1().equals(param.field1()) &&
                a.getStatus() != XxxStatus.CANCELLED);
        if (conflict) throw new IllegalStateException("冲突：该条件已存在");
    }

    public record CheckParam(String field1) {}
}
```

## 要点

- Domain Service 处理跨聚合的业务校验，不在任何一个聚合根内
- 不依赖 Spring，纯 Java
- 在 AppService 中组合使用
