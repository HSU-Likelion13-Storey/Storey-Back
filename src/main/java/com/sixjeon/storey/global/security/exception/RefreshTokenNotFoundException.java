package com.sixjeon.storey.global.security.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class RefreshTokenNotFoundException extends BaseException {
    public RefreshTokenNotFoundException() {
        super(RefreshTokenErrorCode.REFRESH_TOKEN_NOT_FOUND_404);
    }
}
