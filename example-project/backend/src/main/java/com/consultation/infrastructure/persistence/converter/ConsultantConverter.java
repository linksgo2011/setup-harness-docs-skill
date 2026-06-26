package com.consultation.infrastructure.persistence.converter;

import com.consultation.domain.aggregate.Consultant;
import com.consultation.infrastructure.persistence.po.ConsultantPO;
import org.springframework.stereotype.Component;

@Component
public class ConsultantConverter {
    public Consultant toDomain(ConsultantPO po) {
        if (po == null) return null;
        return Consultant.builder().id(po.getId()).name(po.getName()).title(po.getTitle()).bio(po.getBio())
                .specialties(po.getSpecialties()).avatar(po.getAvatar()).branchId(po.getBranchId())
                .status(Consultant.ConsultantStatus.valueOf(po.getStatus())).sortOrder(po.getSortOrder()).build();
    }
    public ConsultantPO toPO(Consultant c) {
        if (c == null) return null;
        ConsultantPO po = new ConsultantPO();
        po.setId(c.getId()); po.setName(c.getName()); po.setTitle(c.getTitle()); po.setBio(c.getBio());
        po.setSpecialties(c.getSpecialties()); po.setAvatar(c.getAvatar()); po.setBranchId(c.getBranchId());
        po.setStatus(c.getStatus().name()); po.setSortOrder(c.getSortOrder());
        return po;
    }
}
