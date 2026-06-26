package com.consultation.adapter.rest;

import com.consultation.application.service.BusinessRuleAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/business-rules")
@RequiredArgsConstructor
public class BusinessRuleController {

    private final BusinessRuleAppService service;

    @GetMapping
    public List<Map<String, Object>> list() {
        return service.getAllRules();
    }
}
