package com.sixjeon.storey.domain.owner.repository;

import com.sixjeon.storey.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByLoginId(String loginId);
}
