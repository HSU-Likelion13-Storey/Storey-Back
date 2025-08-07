package com.sixjeon.storey.domain.owner.entity;

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
    // 사장님 이름 -> 회원가입 받을 때 입력받지 않으므로 nullable = true로 설정
    @Column(name = "representative_name",nullable = true)
    private String representativeName;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Builder
    public Owner( String loginId, String password, String phoneNumber) {
        this.loginId = loginId;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }



}
