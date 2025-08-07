package com.sixjeon.storey.domain.user.repository;

import com.sixjeon.storey.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByLoginId(String loginId);
    Boolean existsByPhoneNumber(String phoneNumber);
}
