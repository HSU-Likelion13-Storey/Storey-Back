package com.sixjeon.storey.domain.user.web.dto;

public record CollectedCharacter(
        Long storeId,
        String storeName,
        String characterImageUrl
) {
}
