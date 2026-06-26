package com.consultation.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordServiceImpl implements com.consultation.domain.service.PasswordService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String raw) { return encoder.encode(raw); }

    @Override
    public boolean matches(String raw, String encoded) { return encoder.matches(raw, encoded); }
}
