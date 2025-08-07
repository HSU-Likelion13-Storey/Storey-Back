package com.sixjeon.storey.domain.auth.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class DuplicateLoginIdException extends BaseException {
    public DuplicateLoginIdException() {
        super(AuthErrorCode.AUTH_DUPLICATE_LOGINID_409);
    }
}
