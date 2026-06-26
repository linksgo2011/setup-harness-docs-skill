package com.consultation.infrastructure.config;

import com.consultation.domain.businessrule.AppointmentRule;
import com.consultation.domain.businessrule.AuthRule;
import com.consultation.domain.businessrule.IBusinessRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessRuleRegistration {

    @Bean
    public IBusinessRule appointmentConflictDetection() {
        return AppointmentRule.CONFLICT_DETECTION;
    }

    @Bean
    public IBusinessRule appointmentSlotInterval() {
        return AppointmentRule.SLOT_INTERVAL;
    }

    @Bean
    public IBusinessRule appointmentAvailableHours() {
        return AppointmentRule.AVAILABLE_HOURS;
    }

    @Bean
    public IBusinessRule appointmentCancelWindow() {
        return AppointmentRule.CANCEL_WINDOW;
    }

    @Bean
    public IBusinessRule authPasswordMinLength() {
        return AuthRule.PASSWORD_MIN_LENGTH;
    }

    @Bean
    public IBusinessRule authTokenExpiration() {
        return AuthRule.TOKEN_EXPIRATION;
    }

    @Bean
    public IBusinessRule authPasswordAlgorithm() {
        return AuthRule.PASSWORD_ALGORITHM;
    }
}
