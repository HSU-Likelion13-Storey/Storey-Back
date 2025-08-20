package com.sixjeon.storey.domain.auth.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenReq {

    @NotBlank
    private String refreshToken;
}
