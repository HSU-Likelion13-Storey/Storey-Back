package com.sixjeon.storey.domain.user.service;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.user.entity.User;
import com.sixjeon.storey.domain.user.repository.UserRepository;
import com.sixjeon.storey.domain.user.web.dto.UserMyPageRes;
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

        User user = User.builder()
                .loginId(signupReq.getLoginId())
                .password(bCryptPasswordEncoder.encode(signupReq.getPassword()))
                .nickName(signupReq.getNickName())
                .build();

        return userRepository.save(user);
    }

    @Override
    public UserMyPageRes getMyPage(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(UserNotFoundException::new);

        return new UserMyPageRes(
                user.getNickName(),
                user.getLoginId()
        );
    }
}
