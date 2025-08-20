package com.sixjeon.storey.domain.interview.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class ImageDataMissingException extends BaseException {
    public ImageDataMissingException() {
        super(AiErrorCode.MISSING_B64_JSON);
    }
}
