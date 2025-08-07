package com.sixjeon.storey.domain.user.service;

import com.sixjeon.storey.domain.user.entity.ProviderType;
import com.sixjeon.storey.domain.auth.exception.DuplicateLoginIdException;
import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.user.entity.User;
import com.sixjeon.storey.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public User createUser(SignupReq signupReq) {
//        if (userRepository.existsByLoginId(signupReq.getLoginId())) {
//            throw new DuplicateLoginIdException();
//        }

        User user = User.builder()
                .loginId(signupReq.getLoginId())
                .password(bCryptPasswordEncoder.encode(signupReq.getPassword()))
                .phoneNumber(signupReq.getPhoneNumber())
                .provider(ProviderType.LOCAL)
                .build();

        return userRepository.save(user);
    }
}
