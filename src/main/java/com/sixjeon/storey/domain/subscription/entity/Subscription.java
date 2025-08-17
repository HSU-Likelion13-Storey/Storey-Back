package com.sixjeon.storey.domain.subscription.entity;

import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.subscription.entity.enums.SubscriptionStatus;
import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;
    // 구독 플랜 이름
    @Column(nullable = false)
    private String planName;
    // 구독 시작일
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    // 구독 만료일
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    // 구독 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    // 구독 상태 변경 메서드
    public void updateStatus(SubscriptionStatus status) {
        this.status = status;
    }
    // 구독을 갱신할 때 만료일을 한 달 뒤로 연장할 수 있는 메서드
    public void renew() {
        this.endDate = this.endDate.plusMonths(1);
        this.status = SubscriptionStatus.ACTIVE;
    }
    // 무료 체험 시작 메소드
    public void startTrial() {
        this.status= SubscriptionStatus.ACTIVE;
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now().plusMonths(1);
    }
}
