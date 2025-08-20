package com.sixjeon.storey.domain.user.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    // 수집한 스토어 Id
    // JSON 배열을 문자열로 저장할거임
    @Column(name = "unlocked_store_ids", columnDefinition = "TEXT")
    private String unlockedStoreIds;

    // JSON -> JAVA 변환
    public Set<Long> getUnlockedStoreIdSet() {
        // null 및 빈 문자열 체크
        if (unlockedStoreIds == null || unlockedStoreIds.trim().isEmpty()) {
            return new HashSet<>();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<Set<Long>> typeRef = new TypeReference<Set<Long>>() {};
            return mapper.readValue(unlockedStoreIds, typeRef);
        } catch (Exception e) {
            return new HashSet<>();
        }
    }


    // QR 스캔 시 해금 목룍에 추가
    public void addUnlockedStoreId(Long storeId) {
        Set<Long> currentIds = getUnlockedStoreIdSet();
        currentIds.add(storeId);
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.unlockedStoreIds = mapper.writeValueAsString(currentIds);
        } catch (Exception e) {

        }
    }

    // 지도에서 lock 과 캐릭터 판단
    public boolean isStoreUnlocked(Long storeId) {
        return getUnlockedStoreIdSet().contains(storeId);

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
