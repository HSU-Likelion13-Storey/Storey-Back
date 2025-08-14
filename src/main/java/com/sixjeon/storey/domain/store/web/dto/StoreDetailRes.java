package com.sixjeon.storey.domain.store.web.dto;

import lombok.Builder;

@Builder
public record StoreDetailRes(
        // 캐릭터 데이터 추가 예정
        Long storeId,
        String storeName,
        String addressMain,
        String eventContent
) {
}
