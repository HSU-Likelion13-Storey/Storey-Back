package com.sixjeon.storey.domain.proprietor.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckProprietorReq {
    @NotBlank(message = "사업자번호가 비어있습니다.")
    private String businessNumber;
}
