package com.sixjeon.storey.domain.owner.service;

import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.owner.entity.Owner;

public interface OwnerService {
    Owner createOwner(SignupReq signupReq);
}
