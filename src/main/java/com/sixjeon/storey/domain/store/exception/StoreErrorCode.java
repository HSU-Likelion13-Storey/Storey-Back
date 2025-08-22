package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseResponseCode {
    STORE_INVALID_BUSINESS_NUMBER_400("STORE_INVALID_BUSINESS_NUMBER_400", 400, "사업자번호가 일치하지 않습니다."),
    STORE_DUPLICATE_BUSINESS_NUMBER_409("STORE_DUPLICATE_BUSINESS_NUMBER_409", 409, "이미 등록된 사업자입니다."),
    STORE_ALREADY_REGISTERED_409("STORE_ALREADY_REGISTERED_409", 409, "이미 등록된 가게가 존재합니다."),
    INVALID_QR_CODE_400("INVALID_QR_CODE_400", 400, "유효하지 않은 QR 코드입니다."),
    STORE_NOT_SUBSCRIBED_400("STORE_NOT_SUBSCRIBED_400", 400, "구독하지 않은 가게입니다."),
    STORE_ALREADY_UNLOCKED_400("STORE_ALREADY_UNLOCKED_400", 400, "이미 해금된 가게입니다."),
    ADDRESS_CONVERSION_FAILED_400("ADDRESS_CONVERSION_FAILED_400", 400, "주소를 좌표로 변활할 수 없습니다. 주소를 다시 확인해주세요"),
    STORE_NOT_FOUND_404("STORE_NOT_FOUND_404", 404, "가게가 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
