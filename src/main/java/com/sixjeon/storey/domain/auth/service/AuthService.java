package com.sixjeon.storey.domain.auth.service;

import com.sixjeon.storey.domain.auth.web.dto.LoginReq;
import com.sixjeon.storey.domain.auth.web.dto.LoginRes;
import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.auth.web.dto.SignupRes;

public interface AuthService {
    // role로 구분 후 회원가입
    SignupRes signUp(SignupReq signupReq);
    // 사장님 로그인
    LoginRes ownerSignin(LoginReq loginReq);
    // 회원 로그인
    LoginRes userSignin(LoginReq loginReq);
}
