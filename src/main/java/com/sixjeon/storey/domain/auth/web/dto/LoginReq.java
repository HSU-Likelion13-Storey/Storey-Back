package com.sixjeon.storey.domain.auth.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginReq {

    @NotBlank(message = "아이디는 필수 입력 값 입니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    private String password;
}
