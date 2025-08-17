package com.sixjeon.storey.domain.subscription.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionErrorCode implements BaseResponseCode {
    SUBSCRIPTION_NOT_FOUND_404("SUBSCRIPTION_NOT_FOUND_404", 404, "구독 정보를 찾을 수 없습니다.");


    private final String code;
    private final int httpStatus;
    private final String message;
}
