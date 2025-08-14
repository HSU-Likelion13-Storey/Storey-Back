package com.sixjeon.storey.domain.store.web.dto;

import lombok.Builder;

@Builder
public record MapStoreRes(
        // 캐릭터 필드 추가 예정
        Long storeId,
        String storeName,
        String addressMain,
        double latitude,
        double longitude,
        String eventContent
) {
}
