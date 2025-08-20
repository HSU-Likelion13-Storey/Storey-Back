package com.sixjeon.storey.domain.store.service;

import com.sixjeon.storey.domain.store.web.dto.MapStoreRes;
import com.sixjeon.storey.domain.store.web.dto.RegisterStoreReq;
import com.sixjeon.storey.domain.store.web.dto.StoreDetailRes;

import java.util.List;

public interface StoreService {
    // 가게 등록
    void registerStore(RegisterStoreReq registerStoreReq, String ownerId);
    /* 사용자별 가게 조회
    * 1. 구독한 가게만 표시
    * 2. 해금 상태에 따라 자물쇠 캐릭터 구분
    * */
    List<MapStoreRes> findAllStoresForUserMap(String userLoginId);
    // 가게 상세 조회
    StoreDetailRes findStoreDetailForUser(Long storeId, String userLoginId);
    // qr 코드 조회
    String getStoreQrCode(String ownerLoginId);

}
