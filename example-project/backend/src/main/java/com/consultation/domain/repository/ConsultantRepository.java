package com.consultation.domain.repository;

import com.consultation.domain.aggregate.Consultant;
import java.util.List;
import java.util.Optional;

public interface ConsultantRepository {
    Optional<Consultant> findById(String id);
    List<Consultant> findAllActive();
    List<Consultant> findAll();
    Consultant save(Consultant c);
    void update(Consultant c);
    void deleteById(String id);
    long count();
    boolean existsById(String id);
}
