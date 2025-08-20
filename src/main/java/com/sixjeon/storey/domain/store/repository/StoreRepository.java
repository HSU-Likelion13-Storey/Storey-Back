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
    // QR 코드 문자열로 가게를 찾아서 반환
    Optional<Store> findByQrCode(String qrCode);
    // QR 코드가 DB에 존재하는지 true/false -> 중복 체크용
    boolean existsByQrCode(String qrCode);



}
