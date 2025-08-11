package com.sixjeon.storey.domain.interview.repository;

import com.sixjeon.storey.domain.interview.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Integer> {
    List<InterviewSession> findByStoreIdOrderByCreatedAtDesc(Long storeId);
}
