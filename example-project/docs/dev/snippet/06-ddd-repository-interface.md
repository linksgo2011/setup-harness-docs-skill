# Repository 接口（Domain 层）

```java
package com.xxx.domain.repository;

import com.xxx.domain.aggregate.Xxx;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface XxxRepository {
    Optional<Xxx> findById(String id);
    List<Xxx> findByUserId(String userId, int page, int pageSize);
    List<Xxx> findByField1(String field1);
    List<Xxx> findAll(int page, int pageSize);
    Xxx save(Xxx xxx);
    void update(Xxx xxx);
    long count();
}
```

## 要点

- 接口定义在 Domain 层，实现放在 Infrastructure 层（依赖反转）
- 方法操作的是 Domain 对象（`Xxx`），而非 PO
- 方法命名贴近业务语言
