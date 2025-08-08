package com.sixjeon.storey.domain.auth.web.dto;

import com.sixjeon.storey.domain.auth.entity.Role;

public record LoginRes(
        Long id,
        String loginId,
        Role role,
        String token
) {
}
