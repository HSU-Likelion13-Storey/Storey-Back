package com.sixjeon.storey.domain.auth.web.dto;

import com.sixjeon.storey.domain.auth.entity.Role;

public record SignupRes(
        Long id,
        String loginId,
        String nickName,
        Role role
) {
}
