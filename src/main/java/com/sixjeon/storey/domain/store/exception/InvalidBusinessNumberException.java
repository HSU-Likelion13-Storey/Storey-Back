package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class InvalidBusinessNumberException extends BaseException {
    public InvalidBusinessNumberException() {
        super(StoreErrorCode.STORE_INVALID_BUSINESS_NUMBER_400);
    }
}
