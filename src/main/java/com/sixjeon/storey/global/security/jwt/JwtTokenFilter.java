package com.sixjeon.storey.global.security.jwt;

import com.sixjeon.storey.global.security.details.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter: 매 요청마다 한 번 실행되는 JWT 필터
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 토큰 추출
        String token = extractToken(request);

        // 토큰이 유효한 경우, DB에서 사용자를 조회하여 SecurityContext에 저장
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 로그인 아이디 추출
            String loginId = jwtTokenProvider.getLoginId(token);
            // 로그인 아이디로 사용자 상세 정보 조회
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
            // spring security의 인증 토큰 객체 생성
            // userDetails: 인증된 사용자 정보, null: 자격증명(이미 검증), authorities: 사용자 권한
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // 현재 요청의 SecurityContext에 인증 정보 넣기
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization"); // Authorization 헤더 가져오기
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 이후의 실제 토큰 값만 사용
        }
        return null; // 형식이 올바르지 않으면 -> null 반환
    }


}