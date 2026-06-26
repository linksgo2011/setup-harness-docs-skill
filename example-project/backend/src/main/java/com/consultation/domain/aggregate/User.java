package com.consultation.domain.aggregate;

import com.consultation.domain.vo.UserRole;
import com.consultation.domain.vo.UserStatus;
import lombok.Builder;
import lombok.Value;

@Value @Builder
public class User {
    String id;
    String email;
    String password;
    String name;
    String phone;
    UserRole role;
    UserStatus status;

    public boolean isAdmin() { return role == UserRole.ROLE_ADMIN; }
    public boolean isActive() { return status == UserStatus.ACTIVE; }
}
