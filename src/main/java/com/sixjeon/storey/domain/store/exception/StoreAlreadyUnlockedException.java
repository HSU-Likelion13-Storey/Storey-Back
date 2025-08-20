package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class StoreAlreadyUnlockedException extends BaseException {
    public StoreAlreadyUnlockedException() {
        super(StoreErrorCode.STORE_ALREADY_UNLOCKED_400);
    }
}
