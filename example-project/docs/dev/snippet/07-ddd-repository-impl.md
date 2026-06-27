# Repository 实现（Infrastructure 层 — MyBatis-Plus）

```java
package com.xxx.infrastructure.repositoryimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.domain.aggregate.Xxx;
import com.xxx.domain.repository.XxxRepository;
import com.xxx.infrastructure.persistence.converter.XxxConverter;
import com.xxx.infrastructure.persistence.mapper.XxxMapper;
import com.xxx.infrastructure.persistence.po.XxxPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class XxxRepositoryImpl implements XxxRepository {

    private final XxxMapper mapper;
    private final XxxConverter converter;

    @Override public Optional<Xxx> findById(String id) {
        return Optional.ofNullable(converter.toDomain(mapper.selectById(id)));
    }

    @Override
    public List<Xxx> findByUserId(String userId, int page, int pageSize) {
        return mapper.selectPage(new Page<>(page, pageSize),
                new LambdaQueryWrapper<XxxPO>()
                    .eq(XxxPO::getUserId, userId)
                    .orderByDesc(XxxPO::getCreatedTime))
                .getRecords().stream()
                .map(converter::toDomain).toList();
    }

    @Override
    public List<Xxx> findByField1(String field1) {
        return mapper.selectList(new LambdaQueryWrapper<XxxPO>()
                .eq(XxxPO::getField1, field1))
                .stream().map(converter::toDomain).toList();
    }

    @Override public List<Xxx> findAll(int page, int pageSize) {
        return mapper.selectPage(new Page<>(page, pageSize), null)
                .getRecords().stream().map(converter::toDomain).toList();
    }

    @Override
    public Xxx save(Xxx xxx) {
        XxxPO po = converter.toPO(xxx);
        po.setCreatedBy(xxx.getUserId());
        po.setCreatedTime(LocalDateTime.now());
        po.setUpdatedBy(xxx.getUserId());
        po.setUpdatedTime(LocalDateTime.now());
        mapper.insert(po);
        return xxx;
    }

    @Override
    public void update(Xxx xxx) {
        XxxPO po = converter.toPO(xxx);
        po.setUpdatedTime(LocalDateTime.now());
        mapper.updateById(po);
    }

    @Override public long count() {
        return mapper.selectCount(null);
    }
}
```

## 要点

- `@Repository` 标记，实现 Domain 层的接口
- 通过 Converter 在 PO 和 Domain 之间转换
- 使用 LambdaQueryWrapper 做类型安全的查询
- 审计字段（createdBy/createdTime/updatedBy/updatedTime）在持久化时设置
