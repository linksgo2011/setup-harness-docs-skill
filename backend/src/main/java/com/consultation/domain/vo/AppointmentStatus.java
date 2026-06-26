package com.consultation.domain.vo;

import com.consultation.domain.datadictionary.Description;

public enum AppointmentStatus implements Description {
    PENDING("待确认"),
    CONFIRMED("已确认"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;
    AppointmentStatus(String description) { this.description = description; }
    @Override public String getCode() { return name(); }
    @Override public String getDescription() { return description; }
}
