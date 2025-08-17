package com.sixjeon.storey.domain.event.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class EventNotFoundException extends BaseException {
    public EventNotFoundException() {
        super(EventErrorCode.EVENT_NOT_FOUND_404);
    }
}
