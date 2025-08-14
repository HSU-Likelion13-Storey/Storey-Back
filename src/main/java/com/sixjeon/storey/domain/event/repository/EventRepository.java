package com.sixjeon.storey.domain.event.repository;

import com.sixjeon.storey.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // 가게 ID로 이벤트 조회
    Optional<Event> findByStoreId(Long storeId);
}
