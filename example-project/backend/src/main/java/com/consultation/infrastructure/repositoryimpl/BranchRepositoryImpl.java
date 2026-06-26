package com.consultation.infrastructure.repositoryimpl;

import com.consultation.domain.aggregate.Branch;
import com.consultation.domain.repository.BranchRepository;
import com.consultation.infrastructure.persistence.converter.BranchConverter;
import com.consultation.infrastructure.persistence.mapper.BranchMapper;
import com.consultation.infrastructure.persistence.po.BranchPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryImpl implements BranchRepository {

    private final BranchMapper mapper;
    private final BranchConverter converter;

    @Override public Optional<Branch> findById(String id) { return Optional.ofNullable(converter.toDomain(mapper.selectById(id))); }
    @Override public List<Branch> findAll() { return mapper.selectList(null).stream().map(converter::toDomain).toList(); }

    @Override
    public Branch save(Branch b) {
        BranchPO po = converter.toPO(b);
        po.setCreatedBy(b.getId()); po.setCreatedTime(LocalDateTime.now()); po.setUpdatedBy(b.getId()); po.setUpdatedTime(LocalDateTime.now());
        mapper.insert(po); return b;
    }

    @Override public void update(Branch b) { BranchPO po = converter.toPO(b); po.setUpdatedTime(LocalDateTime.now()); mapper.updateById(po); }
    @Override public void deleteById(String id) { mapper.deleteById(id); }
    @Override public long count() { return mapper.selectCount(null); }
}
