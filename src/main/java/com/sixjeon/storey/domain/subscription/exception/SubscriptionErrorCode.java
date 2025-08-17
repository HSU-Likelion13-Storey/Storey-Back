package com.sixjeon.storey.domain.subscription.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionErrorCode implements BaseResponseCode {
    SUBSCRIPTION_NOT_FOUND_404("SUBSCRIPTION_NOT_FOUND_404", 404, "구독 정보를 찾을 수 없습니다."),
    SUBSCRIPTION_INVALID_STATUS_409("SUBSCRIPTION_INVALID_STATUS_409", 409, "무료 체험을 시작할 수 없는 상태입니다.");


    private final String code;
    private final int httpStatus;
    private final String message;
}
