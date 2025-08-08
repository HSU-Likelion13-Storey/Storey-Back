package com.sixjeon.storey.domain.user.service;

import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.user.entity.User;

public interface UserService {
    User createUser(SignupReq signupReq);
}
