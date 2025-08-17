package com.sixjeon.storey.global.external.toss.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements BaseResponseCode {
    PAYMENT_FAILED_EXCEPTION_400("PAYMENT_FAILED_EXCEPTION_400",400,"결제를 실패했습니다.");


    private final String code;
    private final int httpStatus;
    private final String message;



}
