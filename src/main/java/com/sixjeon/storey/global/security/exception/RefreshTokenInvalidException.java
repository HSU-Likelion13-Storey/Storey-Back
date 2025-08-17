package com.sixjeon.storey.global.security.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class RefreshTokenInvalidException extends BaseException {
    public RefreshTokenInvalidException() {
        super(RefreshTokenErrorCode.REFRESH_TOKEN_INVALID_400);
    }
}
