package com.sixjeon.storey.domain.event.entity;

import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="event_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // 이벤트 내용 수정을 위한 편의 메소드
    public void updateContent(String content){
        this.content = content;
    }


}
