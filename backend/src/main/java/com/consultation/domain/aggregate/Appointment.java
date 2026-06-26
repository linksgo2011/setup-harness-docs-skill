package com.consultation.domain.aggregate;

import com.consultation.domain.vo.AppointmentStatus;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;

@Value @Builder
public class Appointment {
    String id; String userId; String consultantId; String branchId;
    LocalDate appointmentDate; String startTime; String endTime;
    int durationMinutes; AppointmentStatus status; String note; String cancelReason;

    public boolean isOwnedBy(String uid) { return userId.equals(uid); }
    public boolean canCancel() { return status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED; }
}
