package com.consultation.infrastructure.persistence.converter;

import com.consultation.domain.aggregate.Branch;
import com.consultation.infrastructure.persistence.po.BranchPO;
import org.springframework.stereotype.Component;

@Component
public class BranchConverter {
    public Branch toDomain(BranchPO po) {
        if (po == null) return null;
        return Branch.builder().id(po.getId()).name(po.getName()).address(po.getAddress())
                .phone(po.getPhone()).status(Branch.BranchStatus.valueOf(po.getStatus())).build();
    }
    public BranchPO toPO(Branch b) {
        if (b == null) return null;
        BranchPO po = new BranchPO();
        po.setId(b.getId()); po.setName(b.getName()); po.setAddress(b.getAddress());
        po.setPhone(b.getPhone()); po.setStatus(b.getStatus().name());
        return po;
    }
}
