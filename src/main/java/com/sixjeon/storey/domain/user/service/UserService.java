package com.sixjeon.storey.domain.user.service;

import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.user.entity.User;
import com.sixjeon.storey.domain.user.web.dto.UserMyPageRes;

public interface UserService {
    User createUser(SignupReq signupReq);
    // 사용자 마이 페이지 조회
    UserMyPageRes getMyPage(String loginId);
}
