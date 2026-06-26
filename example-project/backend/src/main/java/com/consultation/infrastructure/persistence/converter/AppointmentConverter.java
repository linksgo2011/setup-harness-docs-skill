package com.consultation.infrastructure.persistence.converter;

import com.consultation.domain.aggregate.Appointment;
import com.consultation.domain.vo.AppointmentStatus;
import com.consultation.infrastructure.persistence.po.AppointmentPO;
import org.springframework.stereotype.Component;

@Component
public class AppointmentConverter {
    public Appointment toDomain(AppointmentPO po) {
        if (po == null) return null;
        return Appointment.builder().id(po.getId()).userId(po.getUserId()).consultantId(po.getConsultantId())
                .branchId(po.getBranchId()).appointmentDate(po.getAppointmentDate())
                .startTime(po.getStartTime()).endTime(po.getEndTime()).durationMinutes(po.getDurationMinutes())
                .status(AppointmentStatus.valueOf(po.getStatus())).note(po.getNote()).cancelReason(po.getCancelReason()).build();
    }
    public AppointmentPO toPO(Appointment a) {
        if (a == null) return null;
        AppointmentPO po = new AppointmentPO();
        po.setId(a.getId()); po.setUserId(a.getUserId()); po.setConsultantId(a.getConsultantId());
        po.setBranchId(a.getBranchId()); po.setAppointmentDate(a.getAppointmentDate());
        po.setStartTime(a.getStartTime()); po.setEndTime(a.getEndTime()); po.setDurationMinutes(a.getDurationMinutes());
        po.setStatus(a.getStatus().name()); po.setNote(a.getNote()); po.setCancelReason(a.getCancelReason());
        return po;
    }
}
