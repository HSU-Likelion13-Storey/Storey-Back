package com.sixjeon.storey.domain.character.entity;

import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "characters")
public class Character extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Long id;
    // 캐릭터 이름
    @Column(nullable = false)
    private String name;
    // 캐릭터 이미지 url
    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;
    // 캐릭터 말풍선
    private String tagline;
    // 캐릭터 설명
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

}
