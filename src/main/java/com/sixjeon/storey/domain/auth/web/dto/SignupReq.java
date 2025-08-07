package com.sixjeon.storey.domain.auth.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupReq {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(max = 12, message = "아이디는 12자 이내여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문자와 숫자만 사용할 수 있습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).*$", message = "비밀번호는 영문자와 숫자를 포함해야 합니다.")
    private String password;

    private String phoneNumber;
}
