package com.sixjeon.storey.domain.auth.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class DuplicatePhoneNumberException extends BaseException {
    public DuplicatePhoneNumberException() {
        super(AuthErrorCode.AUTH_DUPLICATE_PHONENUMBER_409);
    }
}
