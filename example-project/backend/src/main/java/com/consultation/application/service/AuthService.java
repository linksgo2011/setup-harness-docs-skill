package com.consultation.application.service;

import com.consultation.application.assembler.UserAssembler;
import com.consultation.domain.aggregate.User;
import com.consultation.domain.repository.UserRepository;
import com.consultation.domain.service.PasswordService;
import com.consultation.domain.vo.UserRole;
import com.consultation.domain.vo.UserStatus;
import com.consultation.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordService passwordService;
    private final JwtTokenProvider tokenProvider;
    private final UserAssembler assembler;

    @Transactional
    public UserAssembler.AuthResponse register(String email, String password, String name, String phone) {
        if (userRepo.existsByEmail(email)) throw new IllegalArgumentException("邮箱已被注册");
        User user = userRepo.save(User.builder().id(UUID.randomUUID().toString()).email(email)
                .password(passwordService.encode(password)).name(name).phone(phone)
                .role(UserRole.ROLE_USER).status(UserStatus.ACTIVE).build());
        return assembler.toAuth(user, tokenProvider.generate(user.getId(), user.getEmail(), user.getRole().name()));
    }

    @Transactional
    public UserAssembler.AuthResponse login(String email, String password) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("邮箱或密码错误"));
        if (!user.isActive()) throw new IllegalStateException("账户已被停用");
        if (!passwordService.matches(password, user.getPassword())) throw new IllegalArgumentException("邮箱或密码错误");
        return assembler.toAuth(user, tokenProvider.generate(user.getId(), user.getEmail(), user.getRole().name()));
    }
}
