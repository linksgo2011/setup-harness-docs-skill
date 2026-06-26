package com.consultation.domain.vo;

import com.consultation.domain.datadictionary.Description;

public enum UserRole implements Description {
    ROLE_USER("普通用户"),
    ROLE_ADMIN("管理员");

    private final String description;
    UserRole(String description) { this.description = description; }
    @Override public String getCode() { return name(); }
    @Override public String getDescription() { return description; }
}
