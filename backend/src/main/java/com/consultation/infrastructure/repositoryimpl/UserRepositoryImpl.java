package com.consultation.infrastructure.repositoryimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.consultation.domain.aggregate.User;
import com.consultation.domain.repository.UserRepository;
import com.consultation.infrastructure.persistence.converter.UserConverter;
import com.consultation.infrastructure.persistence.mapper.UserMapper;
import com.consultation.infrastructure.persistence.po.UserPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper mapper;
    private final UserConverter converter;

    @Override
    public Optional<User> findById(String id) { return Optional.ofNullable(converter.toDomain(mapper.selectById(id))); }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(converter.toDomain(mapper.selectOne(new LambdaQueryWrapper<UserPO>().eq(UserPO::getEmail, email))));
    }

    @Override
    public List<User> findAll() { return mapper.selectList(null).stream().map(converter::toDomain).toList(); }

    @Override
    public User save(User u) {
        UserPO po = converter.toPO(u);
        po.setCreatedBy(u.getId()); po.setCreatedTime(LocalDateTime.now());
        po.setUpdatedBy(u.getId()); po.setUpdatedTime(LocalDateTime.now());
        mapper.insert(po);
        return u;
    }

    @Override
    public void update(User u) {
        UserPO po = converter.toPO(u);
        po.setUpdatedTime(LocalDateTime.now());
        mapper.updateById(po);
    }

    @Override
    public boolean existsByEmail(String email) { return mapper.selectCount(new LambdaQueryWrapper<UserPO>().eq(UserPO::getEmail, email)) > 0; }
}
