package com.sixjeon.storey.domain.collection.web.dto;

import lombok.Builder;

@Builder
public record CollectedCharacterRes(
        Long characterId,
        Long storeId,
        String storeName,
        String characterImageUrl
) {
}
