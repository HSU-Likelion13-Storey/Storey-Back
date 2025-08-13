package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class NotFoundStoreException extends BaseException {
  public NotFoundStoreException() {
    super(StoreErrorCode.STORE_NOT_FOUND_404);
  }
}
