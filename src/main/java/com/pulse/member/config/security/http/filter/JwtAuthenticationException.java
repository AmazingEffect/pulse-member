package com.pulse.member.config.security.http.filter;

/**
 * JWT 관련 인증 예외 처리 클래스 (커스텀 예외)
 */
public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
