package com.sixjeon.storey.domain.interview.entity;

import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "interview_session")
@Getter
@Setter
@NoArgsConstructor
public class InterviewSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    // Large Object 뜻.
    @Lob
    private String narrativeSummary;
}
