package com.sixjeon.storey.domain.interview.repository;

import com.sixjeon.storey.domain.interview.entity.InterviewPair;
import com.sixjeon.storey.domain.interview.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewPairRepository extends JpaRepository<InterviewPair, Integer> {
    List<InterviewPair> findBySessionOrderByStepNoAsc(InterviewSession session);
}
