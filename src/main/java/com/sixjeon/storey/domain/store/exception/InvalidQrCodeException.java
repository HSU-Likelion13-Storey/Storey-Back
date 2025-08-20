package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class InvalidQrCodeException extends BaseException {
    public InvalidQrCodeException() {
        super(StoreErrorCode.INVALID_QR_CODE_400);
    }
}
