package com.sixjeon.storey.domain.interview.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class ResponseException extends BaseException {
    public ResponseException() {
        super(AiErrorCode.OPENAI_ERROR_500);
    }
}
