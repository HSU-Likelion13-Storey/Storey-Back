package com.sixjeon.storey.domain.event.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventErrorCode implements BaseResponseCode {
    EVENT_NOT_FOUND_404("EVENT_NOT_FOUND_404", 404, "이벤트를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
