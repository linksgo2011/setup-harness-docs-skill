package com.consultation.domain.datadictionary;

import com.consultation.domain.aggregate.Branch;
import com.consultation.domain.aggregate.Consultant;
import com.consultation.domain.vo.AppointmentStatus;
import com.consultation.domain.vo.UserRole;
import com.consultation.domain.vo.UserStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DemoDataDictionaryType implements DataDictionaryType {
    USER_ROLE(UserRole.class, "用户角色"),
    USER_STATUS(UserStatus.class, "用户状态"),
    APPOINTMENT_STATUS(AppointmentStatus.class, "预约状态"),
    CONSULTANT_STATUS(Consultant.ConsultantStatus.class, "咨询师状态"),
    BRANCH_STATUS(Branch.BranchStatus.class, "网点状态");

    private final Class<? extends Description> clazz;
    private final String description;

    @Override public Class<? extends Description> getClazz() { return clazz; }
    @Override public String getDescription() { return description; }
}
