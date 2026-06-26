package com.consultation.domain.aggregate;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class Branch {
    String id; String name; String address; String phone; BranchStatus status;
    public enum BranchStatus implements com.consultation.domain.datadictionary.Description {
        ACTIVE("营业中"), DISABLED("已关闭");
        private final String description;
        BranchStatus(String description) { this.description = description; }
        @Override public String getCode() { return name(); }
        @Override public String getDescription() { return description; }
    }
}
