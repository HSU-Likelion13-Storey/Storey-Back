package com.sixjeon.storey.global.s3.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class FileSizeExceededException extends BaseException {
  public FileSizeExceededException() {
    super(S3ErrorCode.S3_FILE_SIZE_EXCEED_400);
  }
}
