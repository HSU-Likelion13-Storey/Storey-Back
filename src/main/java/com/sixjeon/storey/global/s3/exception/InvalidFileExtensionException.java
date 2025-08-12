package com.sixjeon.storey.global.s3.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class InvalidFileExtensionException extends BaseException {
    public InvalidFileExtensionException() {
        super(S3ErrorCode.S3_INVALID_EXTENSION_400);
    }
}
