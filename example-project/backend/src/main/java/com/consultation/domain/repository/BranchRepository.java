package com.consultation.domain.repository;

import com.consultation.domain.aggregate.Branch;
import java.util.List;
import java.util.Optional;

public interface BranchRepository {
    Optional<Branch> findById(String id);
    List<Branch> findAll();
    Branch save(Branch b);
    void update(Branch b);
    void deleteById(String id);
    long count();
}
