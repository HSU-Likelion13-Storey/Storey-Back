package com.sixjeon.storey.domain.subscription.repository;

import com.sixjeon.storey.domain.subscription.entity.Subscription;
import com.sixjeon.storey.domain.subscription.entity.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByOwnerId(Long ownerId);
    // 만료일이 특정 날짜이고 활성 상태인 구독 조회
    @Query("SELECT s FROM Subscription s WHERE DATE(s.endDate) = :date AND s.status = : status AND s.owner.billingKey IS NOT NULL")
    List<Subscription> findActiveSubscriptionEndingOn(@Param("date") LocalDate date, @Param("status") SubscriptionStatus status);
}
