package com.sixjeon.storey.domain.event.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveEventReq {
    @NotBlank(message = "이벤트 내용은 비워둘 수 없습니다.")
    private String content;
}
