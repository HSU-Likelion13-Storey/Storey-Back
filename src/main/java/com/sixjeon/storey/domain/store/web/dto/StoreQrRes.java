package com.sixjeon.storey.domain.store.web.dto;

import lombok.Builder;

@Builder
public record StoreQrRes(
        Long characterId,
        String qrCode
) {
}
