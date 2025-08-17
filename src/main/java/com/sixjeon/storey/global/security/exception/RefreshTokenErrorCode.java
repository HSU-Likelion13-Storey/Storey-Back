package com.sixjeon.storey.global.security.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RefreshTokenErrorCode implements BaseResponseCode {
    REFRESH_TOKEN_EXPIRED_401("REFRESH_TOKEN_EXPIRED_401",401,"리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_INVALID_400("REFRESH_TOKEN_INVALID_400",400,"유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND_404("REFRESH_TOKEN_NOT_FOUND_404",404,"리프레시 토큰를 찾을 수 없습니다.");


    private final String code;
    private final int httpStatus;
    private final String message;
}
