package com.consultation.domain.businessrule;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AppointmentRule implements IBusinessRule {
    CONFLICT_DETECTION("appointment_conflict_detection", "同一咨询师同一时段不可重复预约"),
    SLOT_INTERVAL("appointment_slot_interval", "预约时段间隔为 60 分钟"),
    AVAILABLE_HOURS("appointment_available_hours", "可预约时段为 09:00-18:00"),
    CANCEL_WINDOW("appointment_cancel_window", "PENDING / CONFIRMED 状态可取消");

    private final String ruleName;
    private final String description;

    @Override public String getRuleName() { return ruleName; }
    @Override public String getDescription() { return description; }
}
