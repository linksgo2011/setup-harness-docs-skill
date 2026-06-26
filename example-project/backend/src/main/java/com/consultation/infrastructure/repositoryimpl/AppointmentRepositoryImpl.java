package com.consultation.infrastructure.repositoryimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consultation.domain.aggregate.Appointment;
import com.consultation.domain.repository.AppointmentRepository;
import com.consultation.infrastructure.persistence.converter.AppointmentConverter;
import com.consultation.infrastructure.persistence.mapper.AppointmentMapper;
import com.consultation.infrastructure.persistence.po.AppointmentPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final AppointmentMapper mapper;
    private final AppointmentConverter converter;

    @Override public Optional<Appointment> findById(String id) { return Optional.ofNullable(converter.toDomain(mapper.selectById(id))); }

    @Override
    public List<Appointment> findByUserId(String userId, int page, int pageSize) {
        return mapper.selectPage(new Page<>(page, pageSize),
                new LambdaQueryWrapper<AppointmentPO>().eq(AppointmentPO::getUserId, userId).orderByDesc(AppointmentPO::getCreatedTime))
                .getRecords().stream().map(converter::toDomain).toList();
    }

    @Override
    public List<Appointment> findByConsultantIdAndDate(String consultantId, LocalDate date) {
        return mapper.selectList(new LambdaQueryWrapper<AppointmentPO>()
                .eq(AppointmentPO::getConsultantId, consultantId).eq(AppointmentPO::getAppointmentDate, date))
                .stream().map(converter::toDomain).toList();
    }

    @Override
    public List<Appointment> findAll(int page, int pageSize) {
        return mapper.selectPage(new Page<>(page, pageSize), null).getRecords().stream().map(converter::toDomain).toList();
    }

    @Override
    public Appointment save(Appointment a) {
        AppointmentPO po = converter.toPO(a);
        po.setCreatedBy(a.getUserId()); po.setCreatedTime(LocalDateTime.now());
        po.setUpdatedBy(a.getUserId()); po.setUpdatedTime(LocalDateTime.now());
        mapper.insert(po); return a;
    }

    @Override public void update(Appointment a) { AppointmentPO po = converter.toPO(a); po.setUpdatedTime(LocalDateTime.now()); mapper.updateById(po); }

    @Override public long count() { return mapper.selectCount(null); }
    @Override public long countByStatus(String s) { return mapper.selectCount(new LambdaQueryWrapper<AppointmentPO>().eq(AppointmentPO::getStatus, s)); }
    @Override public long countByUserId(String u) { return mapper.selectCount(new LambdaQueryWrapper<AppointmentPO>().eq(AppointmentPO::getUserId, u)); }
}
