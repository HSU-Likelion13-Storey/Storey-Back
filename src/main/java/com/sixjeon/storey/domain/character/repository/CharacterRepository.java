package com.sixjeon.storey.domain.character.repository;

import com.sixjeon.storey.domain.character.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    // storeId로 캐릭터 찾기
    Optional<Character> findByStoreId(Long storeId);
    // 가게에 캐릭터가 있는지 확인 메소드
    boolean existsByStoreId(Long storeId);
}
