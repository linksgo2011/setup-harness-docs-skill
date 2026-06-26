package com.consultation.domain.vo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
    public int durationMinutes() { return (int) Duration.between(startTime, endTime).toMinutes(); }
}
