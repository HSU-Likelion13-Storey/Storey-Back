package com.sixjeon.storey.domain.interview.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class S3UploadException extends BaseException {
    public S3UploadException() {
        super(AiErrorCode.S3_UPLOAD_FAILED_500);
    }
}
