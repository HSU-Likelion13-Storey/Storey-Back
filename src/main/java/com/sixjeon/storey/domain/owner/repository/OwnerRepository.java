package com.sixjeon.storey.domain.owner.repository;

import com.sixjeon.storey.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByPhoneNumber(String phoneNumber);
    // 사장님 로그인 ID로 조회 기능 추가
    Optional<Owner> findByLoginId(String LoginId);
}
