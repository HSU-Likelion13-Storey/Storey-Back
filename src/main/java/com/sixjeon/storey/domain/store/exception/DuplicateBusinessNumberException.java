package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class DuplicateBusinessNumberException extends BaseException {
    public DuplicateBusinessNumberException() {
        super(StoreErrorCode.STORE_DUPLICATE_BUSINESS_NUMBER_409);
    }
}
