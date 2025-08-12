package com.sixjeon.storey.global.s3.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements BaseResponseCode {

    S3_FILE_EMPTY_400("S3_FILE_EMPTY_400", 400, "파일이 비어있습니다."),
    S3_FILE_SIZE_EXCEED_400("S3_FILE_SIZE_EXCEED_400", 400, "파일 크기가 10MB를 초과할 수 없습니다."),
    S3_INVALID_EXTENSION_400("S3_INVALID_EXTENSION_400", 400, "파일의 확장자가 잘못되었습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
