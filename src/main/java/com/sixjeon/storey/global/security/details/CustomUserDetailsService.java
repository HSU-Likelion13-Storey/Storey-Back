package com.sixjeon.storey.global.security.details;

import com.sixjeon.storey.domain.auth.exception.UserNotFoundException;
import com.sixjeon.storey.domain.owner.repository.OwnerRepository;
import com.sixjeon.storey.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // Owner 테이블에서 먼저 조회
        return ownerRepository.findByLoginId(loginId)
                .<UserDetails>map(CustomUserDetails::new)
                .orElseGet(() ->
                        // 없으면 User 테이블에서 조회
                        userRepository.findByLoginId(loginId)
                        .<UserDetails>map(CustomUserDetails::new)
                        .orElseThrow(() -> new UserNotFoundException()));
    }
}
