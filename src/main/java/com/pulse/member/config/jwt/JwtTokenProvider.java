package com.pulse.member.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
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
     * @param email    사용자 이메일
     * @param nickname 사용자 닉네임
     * @return 생성된 JWT Access 토큰
     * @apiNote JWT Access 토큰을 생성하는 메서드
     */
    public String generateAccessToken(String email, String nickname) {
        // 시간 정보 생성
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // JWT 토큰 생성
        return Jwts.builder()
                .subject(email)
                .claim("nickname", nickname)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }


    /**
     * @return 생성된 JWT Access 토큰
     * @apiNote JWT Access 토큰을 재발행하는 메서드
     */
    public String regenerateAccessToken(String email, String nickname) {
        // 시간 정보 생성
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // JWT 토큰 생성
        return Jwts.builder()
                .subject(email)
                .claim("nickname", nickname)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }


    /**
     * @param token JWT 토큰
     * @return 사용자 이름(이메일)
     * @apiNote JWT 토큰에서 사용자 이름(이메일)을 추출하는 메서드
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    /**
     * @param token JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     * @apiNote JWT 토큰의 유효성을 검증하는 메서드
     */
    public boolean validateJwtToken(String token) {
        try {
            // JWT 토큰 파싱
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 만료 시간 검증
            Date expiration = claims.getExpiration();
            if (expiration == null) {
                throw new JwtException("JWT token does not have an expiration date.");
            }

            // 만료 시간이 현재 시간보다 이전인지 확인
            if (expiration.before(Date.from(Instant.now()))) {
                throw new ExpiredJwtException(null, claims, "JWT token is expired.");
            }

            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }


    /**
     * @param token gRPC 토큰
     * @return 추출된 Authentication 객체
     * @apiNote gRPC 토큰에서 Authentication 객체를 추출하는 메서드
     */
    public Authentication getAuthenticationFromGrpcToken(String token) {
        if (validateJwtToken(token)) {
            String email = getEmailFromJwtToken(token);
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        }
        return null;
    }

}
