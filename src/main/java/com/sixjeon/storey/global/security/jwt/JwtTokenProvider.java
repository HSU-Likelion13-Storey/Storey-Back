package com.sixjeon.storey.global.security.jwt;

import com.sixjeon.storey.domain.auth.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private SecretKey key;

    // Base 64 인코딩된 secretKey를 디코딩하여 Secretkey 객체로 변환
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public String generateAccessToken(Long id, String loginId, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        // JWT 빌더를 사용하여 토큰 생성
        return Jwts.builder()
                // claim : 토큰 안에 넣을 정보들
                .claim("id", id)
                .claim("role", role.name())
                // jwt 토큰 주체
                .subject(loginId)
                // 토큰 발급 시간
                .issuedAt(now)
                // 토큰 만료 시간
                .expiration(expiry)
                // 비밀키로 서명
                .signWith(key)
                // 토큰 생성
                .compact();
    }

    // refresh 토큰 설정
    public String generateRefreshToken(String loginId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpiration);
        return Jwts.builder()
                .claim("tokenType", "REFRESH")
                .subject(loginId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }


    /*
    * JWT 토큰 유효성과 만료일자 검증
    * 토큰이 유효하면 true, 유효하지 않면 false
    * */
    public boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){

            return false;
        }
    }

    // 토큰을 파싱하고 Claims 객체로 변환
    public Claims getClaims(String token) {
        return Jwts.parser()
                // 서명에 사용할 키 설정
                .verifyWith(key)
                .build()
                .parseSignedClaims(token) // 서명 검증 + 만료 검증 + 파싱!!
                .getPayload();
    }
    
    // 토큰에서 로그인 아이디 추출
    public String getLoginId(String token) {
        return getClaims(token).getSubject();
    }

}
