package com.sixjeon.storey.domain.auth.web.dto;

public record LoginRes(
        String accessToken,
        String refreshToken
) {
}
