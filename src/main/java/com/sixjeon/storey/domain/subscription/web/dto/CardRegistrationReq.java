package com.sixjeon.storey.domain.subscription.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardRegistrationReq {
    // 무료 체험 중 카드 등록 성공 후 요청 값
    @NotBlank(message = "고객 키는 필수입니다")
    String customerKey;
}
