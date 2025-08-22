package com.sixjeon.storey.global.external.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DocumentDto {

    @JsonProperty("x")
    private Double longitude;

    @JsonProperty("y")
    private Double latitude;

}
