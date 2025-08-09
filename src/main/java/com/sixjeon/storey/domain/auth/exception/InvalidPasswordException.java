package com.sixjeon.storey.domain.auth.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException() {
        super(AuthErrorCode.AUTH_INVALID_PASSWORD_401);
    }
}
