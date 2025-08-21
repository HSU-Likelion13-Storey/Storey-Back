package com.sixjeon.storey.domain.character.exception;

import com.sixjeon.storey.global.response.code.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterErrorCode implements BaseResponseCode {
    CHARACTER_NOT_FOUND_404("CHARACTER_NOT_FOUND_404", 404, "캐릭터를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
