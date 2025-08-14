package com.sixjeon.storey.domain.owner.service;

import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
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
                .build();

        return ownerRepository.save(owner);

    }
}
