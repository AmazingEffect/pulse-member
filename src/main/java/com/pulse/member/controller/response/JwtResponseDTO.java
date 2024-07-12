package com.pulse.member.controller.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtResponseDTO {

    private String token;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    // factory method
    public static JwtResponseDTO of(String token, String email, Collection<? extends GrantedAuthority> authorities) {
        return new JwtResponseDTO(token, email, authorities);
    }

}
