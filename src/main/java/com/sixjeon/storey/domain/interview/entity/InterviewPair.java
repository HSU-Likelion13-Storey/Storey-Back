package com.sixjeon.storey.domain.interview.entity;

import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "interview_pair",
        indexes = @Index(name = "idx_pair_session_step", columnList = "session_id, stepNo"))
@Getter
@Setter
@NoArgsConstructor
public class InterviewPair extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pair_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    @Column(nullable = false)
    private Long stepNo;

    @Lob
    private String question;

    @Lob
    private String answer;

}
