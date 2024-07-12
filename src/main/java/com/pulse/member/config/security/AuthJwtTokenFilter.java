package com.pulse.member.config.security;

import com.pulse.member.config.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * JWT 토큰 필터링 클래스
 * HTTP 요청이 들어올 때마다 JWT 토큰을 확인하고, 유효한 토큰인 경우 사용자 인증 정보를 설정하는 필터입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthJwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 요청을 필터링하는 메서드
     *
     * @param request     HttpServletRequest 객체
     * @param response    HttpServletResponse 객체
     * @param filterChain FilterChain 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // 1. JWT 토큰을 요청에서 추출
            String jwt = parseJwt(request);
            if (jwt != null && jwtTokenProvider.validateJwtToken(jwt)) {
                // 2. JWT 토큰이 유효한 경우, 이메일을 추출
                String email = jwtTokenProvider.getEmailFromJwtToken(jwt);

                // 3. 이메일을 사용하여 사용자 정보를 로드하고 authToken에 세팅 (이때 토큰의 principal에 UserDetailsImpl 값이 세팅)
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, roles);

                // 4. SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            log.error("Failed to set user authentication: {}", e.getMessage());
        }

        // 5. 다음 필터 실행
        filterChain.doFilter(request, response);
    }


    /**
     * HTTP 요청에서 JWT 토큰을 추출하는 메서드
     *
     * @param request HttpServletRequest 객체
     * @return 추출된 JWT 토큰 문자열, 없으면 null
     */
    private String parseJwt(HttpServletRequest request) {
        // 1. Authorization 헤더에서 JWT 토큰을 추출
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // 2. "Bearer " 접두사를 제거하고 JWT 토큰 반환
            return headerAuth.substring(7);
        }

        return null;
    }

}
