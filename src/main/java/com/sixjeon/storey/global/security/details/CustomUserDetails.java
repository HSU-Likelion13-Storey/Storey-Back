package com.sixjeon.storey.global.security.details;

import com.sixjeon.storey.domain.auth.entity.enums.Role;
import com.sixjeon.storey.domain.owner.entity.Owner;
import com.sixjeon.storey.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final String loginId;
    private final String password;
    private final Role role;

    // Owner 엔티티로부터 CustomUserDetails 생성
    public CustomUserDetails(Owner owner) {
        this.loginId = owner.getLoginId();
        this.password = owner.getPassword();
        this.role = Role.OWNER;
    }

    // User 엔티티로부터 CustomUserDetails 생성
    public CustomUserDetails(User user) {
        this.loginId = user.getLoginId();
        this.password = user.getPassword();
        this.role = Role.USER;
    }
    // 사용자 권한 목록 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
