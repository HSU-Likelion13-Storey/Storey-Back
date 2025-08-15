package com.sixjeon.storey.domain.subscription.web.dto;

import java.time.LocalDateTime;

public record SubscriptionRes(
        String planName,
        String status,
        LocalDateTime endDate,
        boolean hasCard
) {
}
