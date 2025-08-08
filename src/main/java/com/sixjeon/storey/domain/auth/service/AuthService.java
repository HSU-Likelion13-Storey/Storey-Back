package com.sixjeon.storey.domain.auth.service;

import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.auth.web.dto.SignupRes;

public interface AuthService {
    SignupRes signUp(SignupReq signupReq);
}
