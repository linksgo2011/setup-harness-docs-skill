package com.consultation.adapter.rest;

import com.consultation.application.assembler.AppointmentAssembler;
import com.consultation.application.assembler.BranchAssembler;
import com.consultation.application.assembler.ConsultantAssembler;
import com.consultation.application.assembler.UserAssembler;
import com.consultation.application.service.AdminService;
import com.consultation.application.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final AppointmentService appointmentService;
    private final ConsultantAssembler consultantAssembler;
    private final UserAssembler userAssembler;
    private final BranchAssembler branchAssembler;
    private final AppointmentAssembler appointmentAssembler;


    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() { return adminService.dashboard(); }

    @GetMapping("/users")
    public List<UserAssembler.UserResponse> users() { return adminService.allUsers(); }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<Void> toggleUserStatus(@PathVariable String id) { adminService.toggleUserStatus(id); return ResponseEntity.ok().build(); }

    @GetMapping("/consultants")
    public ResponseEntity<List<ConsultantAssembler.ConsultantResponse>> consultants() {
        return ResponseEntity.ok(adminService.allConsultants().stream().map(consultantAssembler::toResponse).toList());
    }

    @GetMapping("/appointments")
    public List<AppointmentAssembler.AppointmentResponse> appointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/branches")
    public List<BranchAssembler.BranchResponse> branches() {
        return adminService.allBranches();
    }

    @PostMapping("/branches")
    public BranchAssembler.BranchResponse createBranch(@RequestBody Map<String, String> body) {
        return adminService.createBranch(body.get("name"), body.get("address"), body.get("phone"));
    }

    @PutMapping("/branches/{id}")
    public ResponseEntity<Void> updateBranch(@PathVariable String id, @RequestBody Map<String, String> body) {
        adminService.updateBranch(id, body.get("name"), body.get("address"), body.get("phone")); return ResponseEntity.ok().build();
    }

    @DeleteMapping("/branches/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable String id) { adminService.deleteBranch(id); return ResponseEntity.ok().build(); }
}
