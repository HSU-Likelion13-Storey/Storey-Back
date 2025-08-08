package com.sixjeon.storey.domain.auth.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class InvalidRoleException extends BaseException {
    public InvalidRoleException() {
      super(AuthErrorCode.ROLE_INVALID_400);

    }
}
