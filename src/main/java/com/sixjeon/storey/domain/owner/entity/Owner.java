package com.sixjeon.storey.domain.owner.entity;

import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.subscription.entity.Subscription;
import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Owner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(name = "billing_key")
    private String billingKey;

    @Column(name = "refresh_token" , length = 500)
    private String refreshToken;

    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    private Store store;

    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    private Subscription subscription;

    // 결제 카드 등록할 때 customerKey를 등록 메서드
    public void registerCard(String billingKey) {
        this.billingKey = billingKey;
    }

    // 카드 등록 여부 확인 메서드
    public boolean hasRegisteredCard() {
        return this.billingKey != null;
    }

    // Refresh Token 업데이트 메서드
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Refresh Token 삭제
    public void deleteRefreshToken() {
        this.refreshToken = null;
    }



}
