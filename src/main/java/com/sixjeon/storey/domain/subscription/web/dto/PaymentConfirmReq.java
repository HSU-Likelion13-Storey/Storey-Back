package com.sixjeon.storey.domain.subscription.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// 구독 만료 후 재구독을 위해 즉시 결제 후 카드 등록
public class PaymentConfirmReq {
    @NotBlank(message = "결제 키는 필수입니다")
    String paymentKey;

    @NotBlank(message = "주문 ID는 필수입니다")
    String orderId;

    @NotNull(message = "결제 금액은 필수입니다.")
    Long amount;
}
