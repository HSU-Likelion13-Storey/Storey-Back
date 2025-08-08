package com.sixjeon.storey.domain.auth.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseResponseCode {
    AUTH_DUPLICATE_LOGINID_409("AUTH_DULICATE_LOGINID_409",409,"이미 존재하는 아이디입니다."),
    AUTH_DUPLICATE_PHONENUMBER_409("AUTH_DUPLICATE_PHONENUMBER_409",409,"이미 존재하는 전화번호 입니다."),
    ROLE_INVALID_400("ROLE_INVALID_400",400,"유효하지 않은 사용자 유형입니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
