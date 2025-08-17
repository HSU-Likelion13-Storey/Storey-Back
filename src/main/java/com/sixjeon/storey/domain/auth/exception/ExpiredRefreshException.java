package com.sixjeon.storey.domain.auth.exception;

public class ExpiredRefreshException extends RuntimeException {
  public ExpiredRefreshException(String message) {
    super(message);
  }
}
