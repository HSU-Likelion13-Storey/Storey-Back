package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class AlreadyRegisterStoreException extends BaseException {
    public AlreadyRegisterStoreException() {
        super(StoreErrorCode.STORE_ALREADY_REGISTERED_409);
    }
}
