package com.consultation.application.service;

import com.consultation.infrastructure.config.BusinessRuleProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BusinessRuleAppService {

    private final BusinessRuleProvider provider;

    public List<Map<String, Object>> getAllRules() {
        return provider.getAllRules();
    }
}
