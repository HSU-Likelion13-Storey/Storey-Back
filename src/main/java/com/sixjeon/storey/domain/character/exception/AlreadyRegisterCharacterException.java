package com.sixjeon.storey.domain.character.exception;

import com.sixjeon.storey.global.exception.BaseException;

public class AlreadyRegisterCharacterException extends BaseException {
    public AlreadyRegisterCharacterException() {
        super(CharacterErrorCode.CHARACTER_ALREADY_REGISTERED_409);
    }
}
