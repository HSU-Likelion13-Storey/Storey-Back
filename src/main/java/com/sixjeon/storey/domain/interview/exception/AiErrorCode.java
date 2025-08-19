package com.sixjeon.storey.domain.interview.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AiErrorCode implements BaseResponseCode {
    OPENAI_ERROR_500("OPENAI_ERROR_500", 500, "OpenAI service error"),
    S3_UPLOAD_FAILED_500("S3_UPLOAD_FAILED_500", 500, "S3 upload failed"),
    EMPTY_RESPONSE("OPENAI_EMPTY_RESPONSE", 500, "OpenAI images error: empty response"),
    MISSING_B64_JSON("OPENAI_MISSING_B64_JSON", 500, "OpenAI images error: response missing 'b64_json'"),
    API_CALL_FAILED("OPENAI_API_CALL_FAILED", 502, "OpenAI API 호출 실패");

    private final String code;
    private final int httpStatus;
    private final String message;
}
