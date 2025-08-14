package com.sixjeon.storey.domain.store.repository;

import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByBusinessNumber(String businessNumber);
    // 등록된 가게가 있는지 확인(사장님은 하나의 가게만 등록할 수 있기 때문)
    boolean existsByOwner(Owner owner);
    // Owner로 가게 조회
    Optional<Store> findByOwner(Owner owner);
}
