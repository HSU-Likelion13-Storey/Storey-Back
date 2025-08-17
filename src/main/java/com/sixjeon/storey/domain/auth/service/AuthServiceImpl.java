package com.sixjeon.storey.domain.auth.service;

import com.sixjeon.storey.domain.auth.entity.Role;
import com.sixjeon.storey.domain.auth.exception.*;
import com.sixjeon.storey.domain.auth.web.dto.LoginReq;
import com.sixjeon.storey.domain.auth.web.dto.LoginRes;
import com.sixjeon.storey.domain.auth.web.dto.SignupReq;
import com.sixjeon.storey.domain.auth.web.dto.SignupRes;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.owner.service.OwnerService;
import com.sixjeon.storey.domain.subscription.entity.Subscription;
import com.sixjeon.storey.domain.subscription.entity.enums.SubscriptionStatus;
import com.sixjeon.storey.domain.subscription.repository.SubscriptionRepository;
import com.sixjeon.storey.domain.user.entity.User;
import com.sixjeon.storey.domain.user.repository.UserRepository;
import com.sixjeon.storey.domain.user.service.UserService;
import com.sixjeon.storey.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final OwnerService ownerService;
    private final UserService userService;
    private final OwnerRepository ownerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public SignupRes signUp(SignupReq signupReq) {
        // 아이디 중복 확인
        if (ownerRepository.existsByLoginId(signupReq.getLoginId()) || userRepository.existsByLoginId(signupReq.getLoginId())) {
            throw new DuplicateLoginIdException();
        }

        // String role을 Role enum로 변환
        Role role;
        try {
            role = Role.valueOf(signupReq.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException();
        }


        switch (role) {
            case OWNER:
                Owner owner = ownerService.createOwner(signupReq);

                // owner 저장
                ownerRepository.save(owner);

                // 추가(1개월 무료 체험 구독)
                Subscription freeTrial = Subscription.builder()
                        .owner(owner) // owner 매핑
                        .planName("MASCOT_BRANDING_PASS")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusMonths(1)) // 만료일은 시작일로부터 1개월 뒤
                        .status(SubscriptionStatus.ACTIVE) // 무료 체험 -> 활성
                        .build();

                // Subscriptio을 DB에 저장
                subscriptionRepository.save(freeTrial);

                // Owner 엔티티에 Subscription dusruf
                owner.setSubscription(freeTrial);

                return new SignupRes(
                        owner.getId(),
                        owner.getLoginId(),
                        owner.getNickName(),
                        Role.OWNER
                );
            case USER:
                User user = userService.createUser(signupReq);

                return new SignupRes(
                        user.getId(),
                        user.getLoginId(),
                        user.getNickName(),
                        Role.USER);
            default:
                throw new InvalidRoleException();
        }
    }

    @Override
    @Transactional
    public LoginRes ownerSignin(LoginReq loginReq) {
        // 사장님 검색
        Owner owner = ownerRepository.findByLoginId(loginReq.getLoginId())
                .orElseThrow(UserNotFoundException::new);
        // 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(loginReq.getPassword(), owner.getPassword())) {
            throw new InvalidPasswordException();
        }

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(
                owner.getId(),
                owner.getLoginId(),
                Role.OWNER
        );

        // Refresh Token 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken(owner.getLoginId());

        // Refresh Token 업데이트
        owner.updateRefreshToken(refreshToken);
        ownerRepository.save(owner);

        return new LoginRes(accessToken, refreshToken);
    }

    @Override
    public LoginRes userSignin(LoginReq loginReq) {
        // 사용자 검색
        User user = userRepository.findByLoginId(loginReq.getLoginId())
                .orElseThrow(UserNotFoundException::new);
        // 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(
                user.getId(),
                user.getLoginId(),
                Role.USER
        );
// Refresh Token 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getLoginId());

        // Refresh Token 업데이트
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new LoginRes(accessToken, refreshToken);
    }
}



