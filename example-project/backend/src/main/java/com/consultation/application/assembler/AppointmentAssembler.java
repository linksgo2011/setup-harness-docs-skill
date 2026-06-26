package com.consultation.application.assembler;

import com.consultation.domain.aggregate.Appointment;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class AppointmentAssembler {
    public AppointmentResponse toResponse(Appointment a) {
        if (a == null) return null;
        return AppointmentResponse.builder().id(a.getId()).userId(a.getUserId()).consultantId(a.getConsultantId())
                .branchId(a.getBranchId()).appointmentDate(a.getAppointmentDate().toString())
                .startTime(a.getStartTime()).endTime(a.getEndTime()).durationMinutes(a.getDurationMinutes())
                .status(a.getStatus().name()).note(a.getNote()).cancelReason(a.getCancelReason()).build();
    }

    @Data @Builder
    public static class AppointmentResponse {
        String id; String userId; String consultantId; String branchId;
        String appointmentDate; String startTime; String endTime;
        int durationMinutes; String status; String note; String cancelReason;
    }
}
