package com.sixjeon.storey.domain.subscription.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionRenewReq {
    @NotBlank(message = "주문ID는 필수입니다.")
    private String orderId;

    @NotBlank(message = "결제 키는 필수입니다.")
    private String paymentKey;

    @NotNull(message = "결제 금액는 필수입니다.")
    @Min(value = 1, message = "결제 금액는 0원 이상이야 합니다.")
    private Long amount;
}
