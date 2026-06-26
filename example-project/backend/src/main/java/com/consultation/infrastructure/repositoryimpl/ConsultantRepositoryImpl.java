package com.consultation.infrastructure.repositoryimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.consultation.domain.aggregate.Consultant;
import com.consultation.domain.repository.ConsultantRepository;
import com.consultation.infrastructure.persistence.converter.ConsultantConverter;
import com.consultation.infrastructure.persistence.mapper.ConsultantMapper;
import com.consultation.infrastructure.persistence.po.ConsultantPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConsultantRepositoryImpl implements ConsultantRepository {

    private final ConsultantMapper mapper;
    private final ConsultantConverter converter;

    @Override public Optional<Consultant> findById(String id) { return Optional.ofNullable(converter.toDomain(mapper.selectById(id))); }
    @Override public List<Consultant> findAll() { return mapper.selectList(null).stream().map(converter::toDomain).toList(); }

    @Override
    public List<Consultant> findAllActive() {
        return mapper.selectList(new LambdaQueryWrapper<ConsultantPO>()
                .eq(ConsultantPO::getStatus, Consultant.ConsultantStatus.ACTIVE.name())
                .orderByAsc(ConsultantPO::getSortOrder)).stream().map(converter::toDomain).toList();
    }

    @Override
    public Consultant save(Consultant c) {
        ConsultantPO po = converter.toPO(c);
        po.setCreatedBy(c.getId()); po.setCreatedTime(LocalDateTime.now()); po.setUpdatedBy(c.getId()); po.setUpdatedTime(LocalDateTime.now());
        mapper.insert(po); return c;
    }

    @Override public void update(Consultant c) { ConsultantPO po = converter.toPO(c); po.setUpdatedTime(LocalDateTime.now()); mapper.updateById(po); }
    @Override public void deleteById(String id) { mapper.deleteById(id); }
    @Override public long count() { return mapper.selectCount(null); }
    @Override public boolean existsById(String id) { return mapper.selectCount(new LambdaQueryWrapper<ConsultantPO>().eq(ConsultantPO::getId, id)) > 0; }
}
