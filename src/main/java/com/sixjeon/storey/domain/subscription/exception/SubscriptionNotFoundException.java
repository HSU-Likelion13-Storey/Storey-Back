package com.sixjeon.storey.domain.subscription.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class SubscriptionNotFoundException extends BaseException {
    public SubscriptionNotFoundException() {
        super(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND_404);
    }
}
