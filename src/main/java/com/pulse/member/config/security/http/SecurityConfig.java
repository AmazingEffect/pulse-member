package com.pulse.member.config.security.http;

import com.pulse.member.config.security.http.filter.JwtTokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스
 * 웹 보안 구성을 정의합니다.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtTokenFilter jwtTokenFilter;


    /**
     * DAO 인증 제공자 설정. (기본 로그인 인증)
     * DaoAuthenticationProvider는 AuthenticationManager의 구성 요소 중 하나로, 실제 사용자 인증을 수행하는 역할을 합니다.
     * DaoAuthenticationProvider는 retrieveUser 메서드를 사용하여 UserDetailsService.loadUserByUsername을 호출합니다.
     * retrieveUser 메서드에서 첫번째 매게변수인 username은 UsernamePasswordAuthenticationToken을 생성할때 담아준 첫번째 매개변수(이메일)가 전달됩니다.
     * new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()) -> (principal, credentials)
     *
     * <p>
     * 1. retrieveUser 메서드를 통해 사용자를 로드합니다.
     * 2. additionalAuthenticationChecks 메서드를 통해 비밀번호를 검증합니다.
     * 3. 비밀번호가 일치하면 createSuccessAuthentication 메서드를 호출하여 인증된 UsernamePasswordAuthenticationToken 객체를 생성합니다
     * 4. 생성된 UsernamePasswordAuthenticationToken 객체는 principal로 UserDetailsImpl 객체를 설정합니다.
     * 5. 로그인 할때 generateToken 메서드로 JWT 토큰을 생성할때 사용자 정보를 가져오기 위해 authentication.getPrincipal() 메서드를 호출합니다.
     * </p>
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // 사용자 세부 정보를 로드할 UserDetailsService 설정
        authProvider.setPasswordEncoder(passwordEncoder());     // 비밀번호를 인코딩 및 검증할 PasswordEncoder 설정
        return authProvider;
    }


    /**
     * 인증 관리자 설정. (Provider 지정 및 인증 처리)
     * AuthenticationManager는 여러 AuthenticationProvider를 사용하여 인증을 처리할 수 있는 중앙 관리자입니다.
     * 지금은 provider로 DaoAuthenticationProvider를 사용하고 있습니다.
     * 그래서 AuthenticationManager.authenticate 메서드가 호출되면 내부적으로 DaoAuthenticationProvider.authenticate를 호출합니다.
     *
     * @param authenticationConfiguration 인증 구성 객체
     * @return AuthenticationManager
     * @throws Exception 예외
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Spring Security가 설정한 AuthenticationManager 반환
    }


    /**
     * 비밀번호 인코더 설정.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     * @throws Exception 예외
     * @apiNote Actuator 경로에 대한 보안 필터 체인 설정
     */
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/actuator/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    /**
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     * @throws Exception 예외
     * @apiNote Spring Security 필터 체인 설정
     * Http 요청에 대한 보안 필터를 설정합니다.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain mainFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/member/auth/**").permitAll()
                            .requestMatchers("/member/auth/signUp").permitAll()
                            .requestMatchers("/member/role/create").permitAll()
                            .requestMatchers("/member/role/createRoles").permitAll()
                            .anyRequest().authenticated();
                })
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
