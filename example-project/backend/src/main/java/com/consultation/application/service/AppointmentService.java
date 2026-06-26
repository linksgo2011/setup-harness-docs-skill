package com.consultation.application.service;

import com.consultation.application.assembler.AppointmentAssembler;
import com.consultation.domain.aggregate.Appointment;
import com.consultation.domain.aggregate.Consultant;
import com.consultation.domain.repository.AppointmentRepository;
import com.consultation.domain.repository.ConsultantRepository;
import com.consultation.domain.service.AppointmentConflictService;
import com.consultation.domain.vo.AppointmentStatus;
import com.consultation.domain.vo.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final ConsultantRepository consultantRepo;
    private final AppointmentConflictService conflictService = new AppointmentConflictService();
    private final AppointmentAssembler assembler;

    public AppointmentAssembler.AppointmentResponse getById(String id) {
        return assembler.toResponse(appointmentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("预约不存在")));
    }

    public List<AppointmentAssembler.AppointmentResponse> getMyAppointments(String userId) {
        return appointmentRepo.findByUserId(userId, 1, 100).stream().map(assembler::toResponse).toList();
    }

    public List<AppointmentAssembler.AppointmentResponse> getAllAppointments() {
        return appointmentRepo.findAll(1, 1000).stream().map(assembler::toResponse).toList();
    }

    @Transactional
    public AppointmentAssembler.AppointmentResponse create(String userId, String consultantId, String branchId,
                                                           String date, String startTime, String endTime, String note) {
        Consultant consultant = consultantRepo.findById(consultantId)
                .orElseThrow(() -> new IllegalArgumentException("咨询师不存在"));
        if (!consultant.isAvailable()) throw new IllegalStateException("该咨询师当前不可预约");

        LocalDate ld = LocalDate.parse(date);
        LocalTime st = LocalTime.parse(startTime);
        LocalTime et = LocalTime.parse(endTime);
        var existing = appointmentRepo.findByConsultantIdAndDate(consultantId, ld);
        conflictService.validateNoConflict(existing, new TimeSlot(ld, st, et));

        Appointment a = appointmentRepo.save(Appointment.builder().id(UUID.randomUUID().toString())
                .userId(userId).consultantId(consultantId).branchId(branchId)
                .appointmentDate(ld).startTime(startTime).endTime(endTime)
                .durationMinutes((int) java.time.Duration.between(st, et).toMinutes())
                .status(AppointmentStatus.PENDING).note(note).build());
        return assembler.toResponse(a);
    }

    @Transactional
    public void cancel(String id, String userId, String reason) {
        Appointment a = appointmentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("预约不存在"));
        if (!a.canCancel()) throw new IllegalStateException("当前状态不允许取消");
        Appointment upd = Appointment.builder().id(a.getId()).userId(a.getUserId()).consultantId(a.getConsultantId())
                .branchId(a.getBranchId()).appointmentDate(a.getAppointmentDate()).startTime(a.getStartTime())
                .endTime(a.getEndTime()).durationMinutes(a.getDurationMinutes()).status(AppointmentStatus.CANCELLED)
                .note(a.getNote()).cancelReason(reason != null ? reason : "用户取消").build();
        appointmentRepo.update(upd);
    }
}
