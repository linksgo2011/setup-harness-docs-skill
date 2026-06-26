package com.consultation.application.service;

import com.consultation.application.assembler.UserAssembler;
import com.consultation.domain.aggregate.User;
import com.consultation.domain.repository.UserRepository;
import com.consultation.domain.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordService passwordService;
    private final UserAssembler assembler;

    public UserAssembler.UserResponse getProfile(String userId) {
        return assembler.toResponse(userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("用户不存在")));
    }

    @Transactional
    public UserAssembler.UserResponse updateProfile(String userId, String name, String phone) {
        User u = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        User upd = User.builder().id(u.getId()).email(u.getEmail()).password(u.getPassword())
                .name(name != null ? name : u.getName()).phone(phone != null ? phone : u.getPhone())
                .role(u.getRole()).status(u.getStatus()).build();
        userRepo.update(upd);
        return assembler.toResponse(upd);
    }

    @Transactional
    public void changePassword(String userId, String oldPw, String newPw) {
        User u = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (!passwordService.matches(oldPw, u.getPassword())) throw new IllegalArgumentException("原密码错误");
        User upd = User.builder().id(u.getId()).email(u.getEmail()).password(passwordService.encode(newPw))
                .name(u.getName()).phone(u.getPhone()).role(u.getRole()).status(u.getStatus()).build();
        userRepo.update(upd);
    }
}
