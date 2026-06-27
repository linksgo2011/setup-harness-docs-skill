# AppService（Application 层）

```java
package com.xxx.application.service;

import com.xxx.application.assembler.XxxAssembler;
import com.xxx.domain.aggregate.Xxx;
import com.xxx.domain.repository.XxxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class XxxService {

    private final XxxRepository xxxRepo;
    private final XxxAssembler assembler;

    public XxxAssembler.XxxResponse getById(String id) {
        return assembler.toResponse(xxxRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("记录不存在")));
    }

    public List<XxxAssembler.XxxResponse> getMyXxx(String userId) {
        return xxxRepo.findByUserId(userId, 1, 100).stream()
                .map(assembler::toResponse).toList();
    }

    @Transactional
    public XxxAssembler.XxxResponse create(String userId, String field1, String field2) {
        // 1. 校验（通过 repository 或 domain service）
        // 2. 构建聚合根
        Xxx xxx = xxxRepo.save(Xxx.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .field1(field1)
                .field2(field2)
                .status(XxxStatus.PENDING)
                .build());
        return assembler.toResponse(xxx);
    }
}
```

## 要点

- `@Transactional` 标记在用例方法上，事务边界在 application 层
- 组合 repository、domain service 完成用例编排
- 使用 assembler 将 domain 转为 DTO 返回给调用方（controller）
- 异常使用 `IllegalArgumentException`（400）或 `IllegalStateException`（409）
