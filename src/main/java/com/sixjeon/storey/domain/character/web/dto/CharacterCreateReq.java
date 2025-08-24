package com.sixjeon.storey.domain.character.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterCreateReq {

    @NotBlank(message = "답변은 필수 입력 값입니다.")
    private String answer;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;
}