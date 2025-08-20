package com.sixjeon.storey.domain.user.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QrScanReq {
    @NotBlank
    private String qrCode;
}
