# PO 与 Mapper（Infrastructure 层 — MyBatis-Plus）

## PO（持久化对象）

```java
package com.xxx.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("xxx")
public class XxxPO {
    private String id;
    private String userId;
    private String field1;
    private String field2;
    private LocalDate date;
    private String status;
    private String note;
    // 审计字段
    private String createdBy;
    private LocalDateTime createdTime;
    private String updatedBy;
    private LocalDateTime updatedTime;
}
```

## Mapper

```java
package com.xxx.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.infrastructure.persistence.po.XxxPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface XxxMapper extends BaseMapper<XxxPO> {}
```

## 要点

- PO 使用 `@Data` + `@TableName`，字段与数据库列一一对应
- 枚举字段存为 `String`（`status`），通过 Converter 与 Domain 枚举互转
- Mapper 继承 `BaseMapper<PO>`，无需手写 SQL
- 主键为 UUID 字符串（`VARCHAR(64)`），由应用层生成
- 审计字段（createdBy/createdTime/updatedBy/updatedTime）在每个 PO 中定义
