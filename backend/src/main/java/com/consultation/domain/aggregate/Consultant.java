package com.consultation.domain.aggregate;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class Consultant {
    String id; String name; String title; String bio; String specialties;
    String avatar; String branchId; ConsultantStatus status; int sortOrder;

    public enum ConsultantStatus implements com.consultation.domain.datadictionary.Description {
        ACTIVE("可预约"), DISABLED("不可预约");
        private final String description;
        ConsultantStatus(String description) { this.description = description; }
        @Override public String getCode() { return name(); }
        @Override public String getDescription() { return description; }
    }
    public boolean isAvailable() { return status == ConsultantStatus.ACTIVE; }
}
