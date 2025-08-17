package com.sixjeon.storey.domain.subscription.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionRenewReq {
    private String orderId;
}
