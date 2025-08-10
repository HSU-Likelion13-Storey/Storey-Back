package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseResponseCode {
    STORE_INVALID_BUSINESS_NUMBER_400("STORE_INVALID_BUSINESS_NUMBER_400", 400, "사업자번호가 일치하지 않습니다."),
    STORE_DUPLICATE_BUSINESS_NUMBER_409("STORE_DUPLICATE_BUSINESS_NUMBER_409", 409, "이미 등록된 사업자입니다."),
    STORE_ALREADY_REGISTERED_409("STORE_ALREADY_REGISTERED_409", 409, "이미 등록된 가게가 존재합니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
