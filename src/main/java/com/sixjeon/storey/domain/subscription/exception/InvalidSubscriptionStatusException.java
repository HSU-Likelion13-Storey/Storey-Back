package com.sixjeon.storey.domain.subscription.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class InvalidSubscriptionStatusException extends BaseException {
    public InvalidSubscriptionStatusException() {
        super(SubscriptionErrorCode.SUBSCRIPTION_INVALID_STATUS_409);
    }
}
