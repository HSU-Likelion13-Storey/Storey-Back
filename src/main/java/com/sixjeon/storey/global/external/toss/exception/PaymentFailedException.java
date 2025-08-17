package com.sixjeon.storey.global.external.toss.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class PaymentFailedException extends BaseException {
    public PaymentFailedException() {
        super(PaymentErrorCode.PAYMENT_FAILED_EXCEPTION_400);
    }
}
