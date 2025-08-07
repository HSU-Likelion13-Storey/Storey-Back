package com.sixjeon.storey.domain.auth.service;

import com.sixjeon.storey.domain.auth.entity.Role;
import com.sixjeon.storey.domain.auth.exception.InvalidRoleException;
import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.auth.web.dto.SignupRes;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.service.OwnerService;
import com.sixjeon.storey.domain.user.entity.User;
import com.sixjeon.storey.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final OwnerService ownerService;
    private final UserService userService;


    @Override
    public SignupRes signUp(SignupReq signupReq) {
        switch (signupReq.getRole()) {
            case OWNER:
                Owner owner = ownerService.createOwner(signupReq);

                return new SignupRes(
                        owner.getId(),
                        owner.getLoginId(),
                        owner.getPhoneNumber(),
                        Role.OWNER
                );
            case USER:
                User user = userService.createUser(signupReq);

                return new SignupRes(
                        user.getId(),
                        user.getLoginId(),
                        user.getPhoneNumber(),
                        Role.USER);
            default:
                throw new InvalidRoleException();
        }
    }
}
