package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class AddressConversionFailedException extends BaseException {
    public AddressConversionFailedException() {
        super(StoreErrorCode.ADDRESS_CONVERSION_FAILED_400);
    }
}
