package com.sixjeon.storey.domain.user.web.dto;

public record CollectedCharacterRes(
        Long storeId,
        String storeName,
        String characterImageUrl
) {
}
