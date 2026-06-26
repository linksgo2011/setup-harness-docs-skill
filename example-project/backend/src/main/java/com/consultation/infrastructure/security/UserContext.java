package com.consultation.infrastructure.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder @AllArgsConstructor
public class UserContext {
    private String userId;
    private String email;
    private String role;
}
