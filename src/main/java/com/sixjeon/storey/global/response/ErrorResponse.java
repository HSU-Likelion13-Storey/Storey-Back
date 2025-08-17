package com.sixjeon.storey.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonPropertyOrder({"isSuccess", "timestamp","code","httpStatus","message","data"})
public class ErrorResponse<T> extends BaseResponse{
    private final int httpStatus;
    private final T data;

    // 단일 생성자 - 모든 경우를 처리
    @Builder
    private ErrorResponse(T data, String code, String message, int httpStatus){
        super(false, code, message);
        this.httpStatus = httpStatus;
        this.data = data;
    }

    // 기본 메시지 사용
    public static ErrorResponse<?> from(BaseResponseCode baseResponseCode){
        return ErrorResponse.builder()
                .code(baseResponseCode.getCode())
                .message(baseResponseCode.getMessage())
                .httpStatus(baseResponseCode.getHttpStatus())
                .data(null)
                .build();
    }

    // 커스텀 메시지
    public static ErrorResponse<?> of(BaseResponseCode baseResponseCode, String message){
        return ErrorResponse.builder()
                .code(baseResponseCode.getCode())
                .message(message)
                .httpStatus(baseResponseCode.getHttpStatus())
                .data(null)
                .build();
    }

    // 데이터 포함 (기본 메시지)
    public static <T> ErrorResponse<T> of(BaseResponseCode baseResponseCode, T data){
        return ErrorResponse.<T>builder()
                .code(baseResponseCode.getCode())
                .message(baseResponseCode.getMessage())
                .httpStatus(baseResponseCode.getHttpStatus())
                .data(data)
                .build();
    }

    // 데이터 + 커스텀 메시지
    public static <T> ErrorResponse<T> of(BaseResponseCode baseResponseCode, T data, String message){
        return ErrorResponse.<T>builder()
                .code(baseResponseCode.getCode())
                .message(message)
                .httpStatus(baseResponseCode.getHttpStatus())
                .data(data)
                .build();
    }
}
