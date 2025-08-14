package com.sixjeon.storey.domain.user.entity;

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

}
