package com.pulse.member.config.jwt;

import com.pulse.member.config.security.http.user.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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

    @Value("${jwt.jwtExpiration}")
    private int jwtExpiration;

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
     * JWT 토큰을 생성하는 메서드
     *
     * @param authentication Authentication 객체
     * @return 생성된 JWT 토큰
     */
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(userDetails.getEmail()) // 이메일을 subject 클레임에 저장
                .claim("roles", userDetails.getAuthorities())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(secretKey) // 서명 생성
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
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
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
