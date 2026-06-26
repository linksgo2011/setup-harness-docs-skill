package com.consultation.infrastructure.security;

public class SecurityUtils {
    public static UserContext currentUser() {
        UserContext c = UserContextHolder.get();
        if (c == null) throw new IllegalStateException("未登录");
        return c;
    }
    public static String currentUserId() { return currentUser().getUserId(); }
    public static boolean isAdmin() {
        UserContext c = UserContextHolder.get();
        return c != null && "ROLE_ADMIN".equals(c.getRole());
    }
}
