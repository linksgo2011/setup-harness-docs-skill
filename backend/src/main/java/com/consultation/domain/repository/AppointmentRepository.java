package com.consultation.domain.repository;

import com.consultation.domain.aggregate.Appointment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    Optional<Appointment> findById(String id);
    List<Appointment> findByUserId(String userId, int page, int pageSize);
    List<Appointment> findByConsultantIdAndDate(String consultantId, LocalDate date);
    List<Appointment> findAll(int page, int pageSize);
    Appointment save(Appointment a);
    void update(Appointment a);
    long count();
    long countByStatus(String status);
    long countByUserId(String userId);
}
