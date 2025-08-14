package com.sixjeon.storey.domain.owner.entity;

import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Owner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;


    @Builder
    public Owner( String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    private Store store;



}
