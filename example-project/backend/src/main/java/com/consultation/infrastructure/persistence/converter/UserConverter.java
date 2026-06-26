package com.consultation.infrastructure.persistence.converter;

import com.consultation.domain.aggregate.User;
import com.consultation.domain.vo.UserRole;
import com.consultation.domain.vo.UserStatus;
import com.consultation.infrastructure.persistence.po.UserPO;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public User toDomain(UserPO po) {
        if (po == null) return null;
        return User.builder().id(po.getId()).email(po.getEmail()).password(po.getPassword())
                .name(po.getName()).phone(po.getPhone())
                .role(UserRole.valueOf(po.getRole())).status(UserStatus.valueOf(po.getStatus())).build();
    }
    public UserPO toPO(User u) {
        if (u == null) return null;
        UserPO po = new UserPO();
        po.setId(u.getId()); po.setEmail(u.getEmail()); po.setPassword(u.getPassword());
        po.setName(u.getName()); po.setPhone(u.getPhone());
        po.setRole(u.getRole().name()); po.setStatus(u.getStatus().name());
        return po;
    }
}
