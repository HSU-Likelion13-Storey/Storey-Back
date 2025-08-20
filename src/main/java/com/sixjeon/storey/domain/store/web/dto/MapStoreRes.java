package com.sixjeon.storey.domain.store.web.dto;

import com.sixjeon.storey.domain.subscription.entity.enums.SubscriptionStatus;
import lombok.Builder;

@Builder
public record MapStoreRes(
        Long storeId,
        String storeName,
        String addressMain,
        Double latitude,
        Double longitude,
        String eventContent,
        boolean isUnlocked,
        String characterImageUrl,
        SubscriptionStatus subscriptionStatus
) {
}
