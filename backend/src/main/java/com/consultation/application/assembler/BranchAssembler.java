package com.consultation.application.assembler;

import com.consultation.domain.aggregate.Branch;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class BranchAssembler {
    public BranchResponse toResponse(Branch b) {
        if (b == null) return null;
        return BranchResponse.builder().id(b.getId()).name(b.getName()).address(b.getAddress())
                .phone(b.getPhone()).status(b.getStatus().name()).build();
    }

    @Data @Builder
    public static class BranchResponse { String id; String name; String address; String phone; String status; }
}
