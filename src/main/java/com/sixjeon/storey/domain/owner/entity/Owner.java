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

    @Column(name = "customer_key")
    private String customerKey;

    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    private Store store;

    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    private Subscription subscription;

    // 결제 카드 등록할 때 customerKey를 등록 메서드
    public void registerCard(String customerKey) {
        this.customerKey = customerKey;
    }

    // 카드 등록 여부 확인 메서드
    public boolean hasRegisteredCard() {
        return this.customerKey != null;
    }



}
