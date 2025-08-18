package com.sixjeon.storey.domain.store.entity;

import com.sixjeon.storey.domain.event.entity.Event;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name",nullable = false)
    private String storeName;

    @Column(name = "representative_name",nullable = false)
    private String representativeName;

    @Column(name = "business_number",nullable = false, unique = true)
    private String businessNumber;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "business_category")
    private String businessCategory;

    @Column(name = "address_main",nullable = false)
    private String addressMain;

    @Column(name = "address_detail",nullable = false)
    private String addressDetail;

    @Column(name = "postal_code",nullable = false)
    private String postalCode;

    private Double latitude;

    private Double longitude;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

//    @OneToOne(mappedBy = "store", fetch = FetchType.LAZY)
//    private Event event;





}
