package com.sixjeon.storey.domain.auth.exception;

import com.sixjeon.storey.global.exception.BaseException;


public class ExpiredRefreshException extends BaseException {
    public ExpiredRefreshException() {
        super(AuthErrorCode.AUTH_EXPIRED_REFRESH_401);
    }
}
