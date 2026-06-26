package com.consultation.domain.businessrule;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthRule implements IBusinessRule {
    PASSWORD_MIN_LENGTH("auth_password_min_length", "密码最少 8 位"),
    TOKEN_EXPIRATION("auth_token_expiration", "JWT 有效期 24 小时"),
    PASSWORD_ALGORITHM("auth_password_algorithm", "密码使用 BCrypt 加密");

    private final String ruleName;
    private final String description;

    @Override public String getRuleName() { return ruleName; }
    @Override public String getDescription() { return description; }
}
