package com.sixjeon.storey.domain.character.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class CharacterNotFoundException extends BaseException {
    public CharacterNotFoundException() {
        super(CharacterErrorCode.CHARACTER_NOT_FOUND_404);
    }
}
