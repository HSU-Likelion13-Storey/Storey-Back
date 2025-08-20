package com.sixjeon.storey.domain.store.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class NotSubscribedException extends BaseException {
    public NotSubscribedException() {
        super(StoreErrorCode.STORE_NOT_SUBSCRIBED_400);
    }
}
