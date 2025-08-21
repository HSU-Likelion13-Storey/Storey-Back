package com.sixjeon.storey.domain.owner.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.owner.web.dto.OwnerMyPageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Owner createOwner(SignupReq signupReq) {

        Owner owner = Owner.builder()
                .loginId(signupReq.getLoginId())
                .password(bCryptPasswordEncoder.encode(signupReq.getPassword()))
                .nickName(signupReq.getNickName())
                .build();

        return ownerRepository.save(owner);

    }

    @Override
    public OwnerMyPageRes getMyPage(String loginId) {
        Owner owner = ownerRepository.findByLoginId(loginId)
                .orElseThrow(UserNotFoundException::new);

        return new OwnerMyPageRes(
                owner.getNickName(),
                owner.getLoginId()
        );
    }
}
