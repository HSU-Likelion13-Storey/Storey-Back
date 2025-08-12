package com.sixjeon.storey.global.s3.exception;

public class FileEmptyException extends RuntimeException {
  public FileEmptyException(String message) {
    super(message);
  }
}
