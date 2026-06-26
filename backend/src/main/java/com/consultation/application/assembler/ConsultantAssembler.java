package com.consultation.application.assembler;

import com.consultation.domain.aggregate.Consultant;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class ConsultantAssembler {
    public ConsultantResponse toResponse(Consultant c) {
        if (c == null) return null;
        return ConsultantResponse.builder().id(c.getId()).name(c.getName()).title(c.getTitle()).bio(c.getBio())
                .specialties(c.getSpecialties()).avatar(c.getAvatar()).branchId(c.getBranchId())
                .status(c.getStatus().name()).sortOrder(c.getSortOrder()).build();
    }

    @Data @Builder
    public static class ConsultantResponse {
        String id; String name; String title; String bio; String specialties;
        String avatar; String branchId; String status; int sortOrder;
    }
}
