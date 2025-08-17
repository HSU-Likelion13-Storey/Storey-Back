package com.sixjeon.storey.global.external.s3.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class FileEmptyException extends BaseException {
  public FileEmptyException() {
    super(S3ErrorCode.S3_FILE_EMPTY_400);
  }
}

