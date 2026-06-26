package com.consultation.domain.service;

public interface PasswordService {
    String encode(String raw);
    boolean matches(String raw, String encoded);
}
