package com.sixjeon.storey.domain.store.repository;

import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.store.entity.Store;
import com.sixjeon.storey.domain.store.web.dto.MapStoreRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByBusinessNumber(String businessNumber);
    // 등록된 가게가 있는지 확인(사장님은 하나의 가게만 등록할 수 있기 때문)
    boolean existsByOwner(Owner owner);
    // Owner로 가게 조회
    Optional<Store> findByOwner(Owner owner);
    /*
    * 지도에 표시할 모든 가게 정보를 조회하는 쿼리
    * Store와 Event를 Left Join함
    * */
    @Query("SELECT new com.sixjeon.storey.domain.store.web.dto.MapStoreRes(s.id, s.storeName, s.addressMain, s.latitude, s.longitude, e.content) FROM Store s LEFT JOIN s.event e")
    List<MapStoreRes> findAllWithEvent();



}
