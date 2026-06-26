package com.consultation.domain.repository;

import com.consultation.domain.aggregate.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User save(User user);
    void update(User user);
    boolean existsByEmail(String email);
}
