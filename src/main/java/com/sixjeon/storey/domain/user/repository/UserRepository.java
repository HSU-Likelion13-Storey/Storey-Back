package com.sixjeon.storey.domain.user.repository;

import com.sixjeon.storey.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByLoginId(String loginId);
    // 사용자 로그인 ID로 조회 기능 추가
    Optional<User> findByLoginId(String LoginId);
    // 토큰 조회
    Optional<User> findByRefreshToken(String refreshToken);
}
