package com.sixjeon.storey.domain.store.service;

import com.sixjeon.storey.domain.store.web.dto.MapStoreRes;
import com.sixjeon.storey.domain.store.web.dto.RegisterStoreReq;
import com.sixjeon.storey.domain.store.web.dto.StoreDetailRes;

import java.util.List;

public interface StoreService {
    // 가게 등록
    void registerStore(RegisterStoreReq registerStoreReq, String ownerId);
    // 지도에서 모든 가게 조회
    List<MapStoreRes> findAllStoresForMap();
    // 지도에서 가게 상세 조회
    StoreDetailRes findStoreDetail(Long storeId);
}
