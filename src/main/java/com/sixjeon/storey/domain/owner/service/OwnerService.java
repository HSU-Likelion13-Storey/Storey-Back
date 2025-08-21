package com.sixjeon.storey.domain.owner.service;

import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.web.dto.OwnerMyPageRes;

public interface OwnerService {
    Owner createOwner(SignupReq signupReq);
    // 사장 마이페이지 조회
    OwnerMyPageRes getMyPage(String loginId);
}
