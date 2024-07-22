package com.pulse.member.config.jwt;

import com.pulse.member.config.security.http.user.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${jwt.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    private SecretKey secretKey;

    /**
     * 객체 초기화 메서드, JWT 비밀 키를 설정
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("The key length should be at least 32 bytes");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes); // Secret Key 생성
    }


    /**
     * JWT Access 토큰을 생성하는 메서드
     *
     * @param authentication Authentication 객체
     * @return 생성된 JWT Access 토큰
     */
    public String generateAccessToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(userDetails.getEmail())
                .claim("roles", userDetails.getAuthorities())
                .claim("nickname", userDetails.getNickname())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }


    /**
     * JWT Refresh 토큰을 생성하는 메서드
     *
     * @param authentication Authentication 객체
     * @return 생성된 JWT Refresh 토큰
     */
    public String generateRefreshToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpirationMs);

        return Jwts.builder()
                .subject(userDetails.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }


    /**
     * JWT 토큰에서 사용자 이름(이메일)을 추출하는 메서드
     *
     * @param token JWT 토큰
     * @return 사용자 이름(이메일)
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    /**
     * JWT 토큰의 유효성을 검증하는 메서드
     *
     * @param token JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey).build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }


    /**
     * gRPC 토큰에서 Authentication 객체를 추출하는 메서드
     *
     * @param token gRPC 토큰
     * @return 추출된 Authentication 객체
     */
    public Authentication getAuthenticationFromGrpcToken(String token) {
        if (validateJwtToken(token)) {
            String email = getEmailFromJwtToken(token);
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        }
        return null;
    }

}
