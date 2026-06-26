package com.consultation.domain.service;

import com.consultation.domain.aggregate.Appointment;
import com.consultation.domain.vo.AppointmentStatus;
import com.consultation.domain.vo.TimeSlot;
import java.util.List;

public class AppointmentConflictService {

    public void validateNoConflict(List<Appointment> existing, TimeSlot requested) {
        boolean conflict = existing.stream().anyMatch(a ->
                a.getAppointmentDate().equals(requested.date()) &&
                a.getStartTime().equals(requested.startTime().toString()) &&
                a.getStatus() != AppointmentStatus.CANCELLED);
        if (conflict) throw new IllegalStateException("该时间段已被预约");
    }
}
