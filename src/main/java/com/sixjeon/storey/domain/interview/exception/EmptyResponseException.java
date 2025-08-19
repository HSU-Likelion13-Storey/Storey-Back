package com.sixjeon.storey.domain.interview.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class EmptyResponseException extends BaseException {
    public EmptyResponseException() {
        super(AiErrorCode.EMPTY_RESPONSE);
    }
}
