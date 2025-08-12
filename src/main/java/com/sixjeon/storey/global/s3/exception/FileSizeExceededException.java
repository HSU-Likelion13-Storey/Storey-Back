package com.sixjeon.storey.global.s3.exception;

public class FileSizeExceededException extends RuntimeException {
  public FileSizeExceededException(String message) {
    super(message);
  }
}
