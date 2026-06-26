package com.consultation.infrastructure.security;

public class UserContextHolder {
    private static final ThreadLocal<UserContext> CTX = new ThreadLocal<>();
    public static void set(UserContext c) { CTX.set(c); }
    public static UserContext get() { return CTX.get(); }
    public static void clear() { CTX.remove(); }
}
