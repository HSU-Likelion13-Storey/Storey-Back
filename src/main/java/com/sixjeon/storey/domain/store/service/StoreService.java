package com.sixjeon.storey.domain.store.service;

import com.sixjeon.storey.domain.store.web.dto.RegisterStoreReq;

public interface StoreService {
    // 가게 등록
    void registerStore(RegisterStoreReq registerStoreReq, String ownerId);
}
