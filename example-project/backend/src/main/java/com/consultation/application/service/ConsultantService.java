package com.consultation.application.service;

import com.consultation.application.assembler.ConsultantAssembler;
import com.consultation.domain.aggregate.Consultant;
import com.consultation.domain.repository.AppointmentRepository;
import com.consultation.domain.repository.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ConsultantService {

    private final ConsultantRepository consultantRepo;
    private final AppointmentRepository appointmentRepo;
    private final ConsultantAssembler assembler;

    public List<ConsultantAssembler.ConsultantResponse> findAllActive() {
        return consultantRepo.findAllActive().stream().map(assembler::toResponse).toList();
    }

    public ConsultantAssembler.ConsultantResponse getById(String id) {
        return assembler.toResponse(consultantRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("咨询师不存在")));
    }

    public List<Map<String, String>> getAvailableSlots(String consultantId, String date) {
        if (!consultantRepo.existsById(consultantId)) return List.of();
        LocalDate localDate = LocalDate.parse(date);
        var booked = new HashSet<String>();
        appointmentRepo.findByConsultantIdAndDate(consultantId, localDate).stream()
                .filter(a -> a.getStatus() != com.consultation.domain.vo.AppointmentStatus.CANCELLED)
                .forEach(a -> booked.add(a.getStartTime()));

        List<Map<String, String>> slots = new ArrayList<>();
        for (int h = 9; h < 18; h++) {
            String s = String.format("%02d:00", h);
            String e = String.format("%02d:00", h + 1);
            if (!booked.contains(s)) slots.add(Map.of("startTime", s, "endTime", e));
        }
        return slots;
    }

    @Transactional
    public ConsultantAssembler.ConsultantResponse create(String name, String title, String bio, String specialties, String avatar, String branchId, int sortOrder) {
        Consultant c = consultantRepo.save(Consultant.builder().id(UUID.randomUUID().toString()).name(name)
                .title(title).bio(bio).specialties(specialties).avatar(avatar).branchId(branchId)
                .status(Consultant.ConsultantStatus.ACTIVE).sortOrder(sortOrder).build());
        return assembler.toResponse(c);
    }

    @Transactional
    public ConsultantAssembler.ConsultantResponse update(String id, String name, String title, String bio, String specialties) {
        Consultant existing = consultantRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("咨询师不存在"));
        Consultant upd = Consultant.builder().id(id).name(name != null ? name : existing.getName())
                .title(title != null ? title : existing.getTitle()).bio(bio != null ? bio : existing.getBio())
                .specialties(specialties != null ? specialties : existing.getSpecialties())
                .avatar(existing.getAvatar()).branchId(existing.getBranchId()).status(existing.getStatus())
                .sortOrder(existing.getSortOrder()).build();
        consultantRepo.update(upd);
        return assembler.toResponse(upd);
    }

    @Transactional
    public void delete(String id) { consultantRepo.deleteById(id); }
}
