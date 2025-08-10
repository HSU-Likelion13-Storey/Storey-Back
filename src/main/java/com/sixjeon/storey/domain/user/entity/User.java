package com.sixjeon.storey.domain.user.entity;

import com.sixjeon.storey.domain.user.entity.enums.ProviderType;
import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    //Local(일반),KAKAO ..
    @Enumerated(EnumType.STRING)
    private ProviderType provider;
    @Column(name = "provider_id")
    private String providerId;





}
