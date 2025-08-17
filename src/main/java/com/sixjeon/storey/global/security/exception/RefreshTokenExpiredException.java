package com.sixjeon.storey.global.security.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class RefreshTokenExpiredException extends BaseException {
    public RefreshTokenExpiredException() {
        super(RefreshTokenErrorCode.REFRESH_TOKEN_EXPIRED_401);
    }
}
