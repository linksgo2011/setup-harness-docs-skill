# 聚合根（Domain 层）

```java
package com.xxx.domain.aggregate;

import com.xxx.domain.vo.XxxStatus;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;

@Value @Builder
public class Xxx {
    String id;
    String userId;
    String field1;
    String field2;
    LocalDate date;
    XxxStatus status;
    String note;

    // 业务守卫方法
    public boolean isOwnedBy(String uid) { return userId.equals(uid); }

    public boolean canCancel() {
        return status == XxxStatus.PENDING || status == XxxStatus.CONFIRMED;
    }
}
```

## 要点

- 使用 `@Value @Builder` 实现不可变对象
- 业务逻辑以守卫方法（guard method）形式封装在聚合根内
- 不含框架注解，纯 Java POJO
- 枚举实现 `Description` 接口用于数据字典注册（见 `04b-ddd-enum.md`）

### 示例：带枚举的聚合根

```java
@Value @Builder
public class Consultant {
    String id; String name; String title; String bio;
    String specialties; String avatar; String branchId;
    ConsultantStatus status; int sortOrder;

    public enum ConsultantStatus implements com.xxx.domain.datadictionary.Description {
        ACTIVE("可预约"), DISABLED("不可预约");

        private final String description;
        ConsultantStatus(String description) { this.description = description; }
        @Override public String getCode() { return name(); }
        @Override public String getDescription() { return description; }
    }

    public boolean isAvailable() { return status == ConsultantStatus.ACTIVE; }
}
```
