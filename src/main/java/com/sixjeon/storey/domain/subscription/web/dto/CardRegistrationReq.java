package com.sixjeon.storey.domain.subscription.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardRegistrationReq {
    // 토스페이먼츠에서 카드 등록 후 받은 authKey
    @NotBlank(message = "인증 키는 필수입니다")
    String authKey;
}
