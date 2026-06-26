package com.consultation.infrastructure.config;

import com.consultation.domain.businessrule.BusinessRuleConfig;
import com.consultation.domain.businessrule.IBusinessRule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BusinessRuleProvider {

    private final BusinessRuleConfig config;
    private final List<IBusinessRule> ruleDefinitions;
    private final Map<String, Map<String, Object>> ruleCache = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        for (IBusinessRule rule : ruleDefinitions) {
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("ruleName", rule.getRuleName());
            detail.put("description", rule.getDescription());

            Map<String, Object> ruleConfig = new LinkedHashMap<>();
            addIfPresent(ruleConfig, "appointment", config.getAppointment());
            addIfPresent(ruleConfig, "auth", config.getAuth());
            if (!ruleConfig.isEmpty()) detail.put("config", ruleConfig);

            ruleCache.put(rule.getRuleName(), detail);
        }
    }

    public List<Map<String, Object>> getAllRules() {
        return new ArrayList<Map<String, Object>>(ruleCache.values());
    }

    private void addIfPresent(Map<String, Object> target, String key, Map<String, Object> source) {
        if (source != null && !source.isEmpty()) target.put(key, source);
    }
}
