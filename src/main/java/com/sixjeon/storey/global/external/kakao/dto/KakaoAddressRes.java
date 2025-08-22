package com.sixjeon.storey.global.external.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoAddressRes {

    @JsonProperty("documents")
    private List<DocumentDto> documents;

}
