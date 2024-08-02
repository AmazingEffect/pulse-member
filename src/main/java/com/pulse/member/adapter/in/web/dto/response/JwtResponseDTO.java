package com.pulse.member.adapter.in.web.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT 토큰 발급 응답 DTO
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public static JwtResponseDTO of(String accessToken, String refreshToken, String email, Collection<? extends GrantedAuthority> authorities) {
        return new JwtResponseDTO(accessToken, refreshToken, email, authorities);
    }

    public static JwtResponseDTO of(String accessToken, String refreshToken, String email) {
        return new JwtResponseDTO(accessToken, refreshToken, email, null);
    }

}
