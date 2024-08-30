package com.pulse.member.config.security.http.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 인증 예외처리 클래스
 * 인증되지 않은 사용자가 보안 HTTP 리소스를 요청하고 AuthenticationException이 발생할 때마다 트리거됩니다.
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * @param request       HttpServletRequest 객체
     * @param response      HttpServletResponse 객체
     * @param authException 발생한 인증 예외
     * @throws IOException 입출력 예외가 발생할 때
     * @apiNote 인증되지 않은 요청에 대한 처리 메서드. (AuthenticationEntryPoint 인터페이스 구현)
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        // 1. 응답의 콘텐츠 타입을 JSON으로 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 2. 응답 상태 코드를 401 Unauthorized로 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 3. 응답 본문에 포함할 데이터 맵 생성 (불변 Map)
        Map<String, Object> body = Map.of(
                "status", HttpServletResponse.SC_UNAUTHORIZED,
                "error", "Unauthorized",
                "message", authException.getMessage(),
                "path", request.getServletPath()
        );

        // 4. ObjectMapper를 사용하여 응답의 출력 스트림에 JSON 데이터 작성
        mapper.writeValue(response.getOutputStream(), body);
    }

}
