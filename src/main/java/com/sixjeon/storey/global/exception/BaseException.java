package com.sixjeon.storey.global.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 비즈니스 레이어에서 예외 상황을 표현하는 커스텀 런타임 예외 클래스
@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException{
    private final BaseResponseCode baseResponseCode;
}
