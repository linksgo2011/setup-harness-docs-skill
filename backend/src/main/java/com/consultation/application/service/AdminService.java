package com.consultation.application.service;

import com.consultation.application.assembler.BranchAssembler;
import com.consultation.application.assembler.UserAssembler;
import com.consultation.domain.aggregate.Branch;
import com.consultation.domain.aggregate.Consultant;
import com.consultation.domain.aggregate.User;
import com.consultation.domain.repository.AppointmentRepository;
import com.consultation.domain.repository.BranchRepository;
import com.consultation.domain.repository.ConsultantRepository;
import com.consultation.domain.repository.UserRepository;
import com.consultation.domain.vo.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepo;
    private final BranchRepository branchRepo;
    private final ConsultantRepository consultantRepo;
    private final AppointmentRepository appointmentRepo;
    private final BranchAssembler branchAssembler;
    private final UserAssembler userAssembler;

    public Map<String, Object> dashboard() {
        return Map.of("totalUsers", userRepo.findAll().size(), "totalConsultants", consultantRepo.count(),
                "totalAppointments", appointmentRepo.count(), "pendingAppointments", appointmentRepo.countByStatus("PENDING"),
                "totalBranches", branchRepo.count());
    }

    public List<UserAssembler.UserResponse> allUsers() { return userRepo.findAll().stream().map(userAssembler::toResponse).toList(); }
    public List<Consultant> allConsultants() { return consultantRepo.findAll(); }
    public List<BranchAssembler.BranchResponse> allBranches() { return branchRepo.findAll().stream().map(branchAssembler::toResponse).toList(); }

    @Transactional
    public void toggleUserStatus(String id) {
        User u = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        User upd = User.builder().id(u.getId()).email(u.getEmail()).password(u.getPassword())
                .name(u.getName()).phone(u.getPhone()).role(u.getRole())
                .status(u.getStatus() == UserStatus.ACTIVE ? UserStatus.DISABLED : UserStatus.ACTIVE).build();
        userRepo.update(upd);
    }

    @Transactional
    public BranchAssembler.BranchResponse createBranch(String name, String address, String phone) {
        return branchAssembler.toResponse(branchRepo.save(Branch.builder().id(UUID.randomUUID().toString())
                .name(name).address(address).phone(phone).status(Branch.BranchStatus.ACTIVE).build()));
    }

    @Transactional
    public void updateBranch(String id, String name, String address, String phone) {
        Branch b = branchRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("网点不存在"));
        branchRepo.update(Branch.builder().id(id).name(name != null ? name : b.getName())
                .address(address != null ? address : b.getAddress()).phone(phone != null ? phone : b.getPhone())
                .status(b.getStatus()).build());
    }

    @Transactional
    public void deleteBranch(String id) { branchRepo.deleteById(id); }
}
