package com.consultation.adapter.rest;

import com.consultation.application.service.AppointmentService;
import com.consultation.infrastructure.security.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(appointmentService.getMyAppointments(SecurityUtils.currentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) { return ResponseEntity.ok(appointmentService.getById(id)); }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateRequest r) {
        return ResponseEntity.ok(appointmentService.create(SecurityUtils.currentUserId(),
                r.consultantId, r.branchId, r.date, r.startTime, r.endTime, r.note));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id, @RequestBody Map<String, String> body) {
        appointmentService.cancel(id, SecurityUtils.currentUserId(), body.getOrDefault("reason", "用户取消"));
        return ResponseEntity.ok().build();
    }

    @Data
    public static class CreateRequest {
        @NotBlank String consultantId;
        String branchId;
        @NotBlank String date;
        @NotBlank String startTime;
        @NotBlank String endTime;
        String note;
    }
}
