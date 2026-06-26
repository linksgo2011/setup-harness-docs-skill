package com.consultation.application.assembler;

import com.consultation.domain.aggregate.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {
    public UserResponse toResponse(User u) {
        if (u == null) return null;
        return UserResponse.builder().id(u.getId()).email(u.getEmail()).name(u.getName())
                .phone(u.getPhone()).role(u.getRole().name()).status(u.getStatus().name()).build();
    }
    public AuthResponse toAuth(User u, String token) {
        return AuthResponse.builder().token(token).user(toResponse(u)).build();
    }

    @Data @Builder
    public static class UserResponse { String id; String email; String name; String phone; String role; String status; }
    @Data @Builder
    public static class AuthResponse { String token; UserResponse user; }
}
