package com.sixjeon.storey.domain.auth.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(AuthErrorCode.USER_NOT_FOUND_404);
    }
}
