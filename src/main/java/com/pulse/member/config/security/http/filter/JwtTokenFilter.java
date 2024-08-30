package com.pulse.member.config.security.http.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.member.config.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * JWT 토큰 필터링 클래스
 * HTTP 요청이 들어올 때마다 JWT 토큰을 확인하고, 유효한 토큰인 경우 사용자 인증 정보를 설정하는 필터입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper mapper = new ObjectMapper();

    // 특정 경로를 필터링에서 제외
    private static final Set<String> EXCLUDE_URLS = Set.of(
            "/member/auth/signUp",
            "/member/auth/signIn",
            "/member/role/create"
    );

    /**
     * @param request     HttpServletRequest 객체
     * @param response    HttpServletResponse 객체
     * @param filterChain FilterChain 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException      입출력 예외
     * @apiNote 모든 요청에 대해 JWT 토큰을 확인하고, 유효한 토큰인 경우 사용자 인증 정보를 설정하는 필터
     * 여기서 알아야 할 것은 authenticationEntryPoint를 통해 예외를 처리하기 위해 AuthenticationException을 상속받은 커스텀 JWT 예외를 사용한다.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 특정 경로를 필터링에서 제외 (config에서 제외 불가능하기에 여기서 처리)
        if (EXCLUDE_URLS.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. JWT 토큰을 요청에서 추출
            String jwt = parseJwt(request);

            // 2. JWT 토큰이 없는 경우 예외 처리
            if (notHasJwtToken(jwt)) {
                throw new JwtAuthenticationException("JWT token is missing");
            }

            // 3. JWT 토큰이 유효한 경우, 사용자 인증 정보 설정
            if (notValidJwtToken(jwt)) {
                throw new JwtAuthenticationException("Invalid JWT token");
            }

            // 4. JWT에 문제가 없다면 사용자 인증 정보 설정
            authenticationFrom(jwt);

        } catch (JwtAuthenticationException e) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, Object> body = Map.of(
                    "status", HttpServletResponse.SC_UNAUTHORIZED,
                    "error", "Unauthorized",
                    "message", e.getMessage(),
                    "path", request.getServletPath()
            );

            mapper.writeValue(response.getOutputStream(), body);
        } catch (JwtException e) {
            log.warn("JWT processing failed: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid JWT token", e);
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", e.getMessage());
            throw new JwtAuthenticationException("User not found", e);
        } catch (Exception e) {
            log.error("Unexpected error during authentication: {}", e.getMessage());
            throw new JwtAuthenticationException("Unexpected error during authentication", e);
        }

        // 3. 다음 필터 실행
        filterChain.doFilter(request, response);
    }


    /**
     * @param request HttpServletRequest 객체
     * @return 추출된 JWT 토큰 문자열, 없으면 null
     * @apiNote HTTP 요청에서 JWT 토큰을 추출하는 메서드
     */
    private String parseJwt(HttpServletRequest request) {
        // 1. Authorization 헤더에서 JWT 토큰을 추출
        String bearerToken = request.getHeader("Authorization");

        // 2. "Bearer " 접두사를 제거하고 JWT 토큰 반환
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 3. JWT 토큰이 없는 경우 null 반환
        return null;
    }


    /**
     * @param jwt JWT 토큰 문자열
     * @return JWT 토큰이 없는 경우 true, 있는 경우 false
     * @apiNote JWT 토큰이 없는 경우를 확인하는 메서드
     */
    private boolean notHasJwtToken(String jwt) {
        return !StringUtils.hasText(jwt);
    }


    /**
     * @param jwt JWT 토큰 문자열
     * @return JWT 토큰이 유효하지 않은 경우 true, 유효한 경우 false
     * @apiNote JWT 토큰이 유효하지 않은 경우를 확인하는 메서드
     */
    private boolean notValidJwtToken(String jwt) {
        return !jwtTokenProvider.validateJwtToken(jwt);
    }


    /**
     * @param jwt JWT 토큰 문자열
     * @apiNote JWT 토큰을 사용하여 사용자 인증 정보를 설정하는 메서드
     */
    private void authenticationFrom(String jwt) {
        // 1. 이메일 추출
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);

        // 2. 사용자 정보 로드 및 인증 설정
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        // 3. SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
