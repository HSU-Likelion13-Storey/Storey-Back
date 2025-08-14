package com.sixjeon.storey.domain.owner.entity;

import com.sixjeon.storey.domain.store.entity.Store;
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



    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    private Store store;



}
