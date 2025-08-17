package com.sixjeon.storey.domain.subscription.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionRenewReq {
    @NotBlank(message = "주문 ID는 필수입니다.")
    private String orderId;
}
