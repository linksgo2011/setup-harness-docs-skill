package com.consultation.domain.businessrule;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "business-rules")
public class BusinessRuleConfig {
    private Map<String, Object> appointment = new HashMap<>();
    private Map<String, Object> auth = new HashMap<>();
}
