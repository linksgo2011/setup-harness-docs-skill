package com.consultation.domain.vo;

import com.consultation.domain.datadictionary.Description;

public enum UserStatus implements Description {
    ACTIVE("正常"),
    DISABLED("已停用");

    private final String description;
    UserStatus(String description) { this.description = description; }
    @Override public String getCode() { return name(); }
    @Override public String getDescription() { return description; }
}
