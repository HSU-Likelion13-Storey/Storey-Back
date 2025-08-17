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
    @Column(nullable = false)
    private String nickName;
    
    @Column(name = "refresh_token" , length = 500)
    private String refreshToken;

    // Refresh Token 업데이트 메서드
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Refresh Token 삭제
    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

}
