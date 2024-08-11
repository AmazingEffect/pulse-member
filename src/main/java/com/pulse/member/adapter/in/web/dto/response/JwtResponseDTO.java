package com.pulse.member.adapter.in.web.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT 토큰 발급 응답 DTO
 */
@Builder
@Data
@NoArgsConstructor
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

    public static JwtResponseDTO of(String accessToken, String refreshToken) {
        return new JwtResponseDTO(accessToken, refreshToken, null, null);
    }

}
