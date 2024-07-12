package com.pulse.member.controller;

import com.pulse.member.config.jwt.JwtTokenProvider;
import com.pulse.member.config.security.UserDetailsImpl;
import com.pulse.member.controller.request.LoginRequestDTO;
import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.controller.response.JwtResponseDTO;
import com.pulse.member.service.usecase.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * 사용자 로그인 처리 메서드.
     *
     * @param loginRequest 로그인 요청 DTO
     * @return JWT 토큰을 포함한 응답
     */
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody LoginRequestDTO loginRequest) {
        // 1. UserDetailsServiceImpl의 loadUserByUsername 메서드를 호출하여 사용자 정보를 로드 (이때 UserDetailsImpl 값이 세팅)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // 2. 인증이 성공하면 SecurityContextHolder에 Authentication 객체를 설정합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. JwtTokenProvider의 generateToken 메서드를 호출하여 JWT 토큰을 생성합니다.
        String jwt = jwtTokenProvider.generateToken(authentication);

        // 4. authentication.getPrincipal() 메서드를 호출하여 UserDetailsImpl 객체에 접근합니다.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(JwtResponseDTO.of(jwt, userDetails.getEmail(), userDetails.getAuthorities()));
    }


    /**
     * 사용자 회원가입 처리 메서드.
     *
     * @param signUpRequest 회원가입 요청 DTO
     * @return 회원가입 성공 메시지
     */
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody MemberSignUpRequestDTO signUpRequest) {
        MemberSignUpRequestDTO registered = memberService.register(signUpRequest);
        return ResponseEntity.ok(registered.getEmail());
    }


    /**
     * 사용자 로그아웃 처리 메서드.
     *
     * @param logoutRequest 로그아웃 요청 DTO
     * @return 로그아웃 성공 메시지
     */
    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(@RequestBody LogoutRequestDTO logoutRequest) {
        memberService.logout(logoutRequest);
        return ResponseEntity.ok("User logged out successfully");
    }

}
