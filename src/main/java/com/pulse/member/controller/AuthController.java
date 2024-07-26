package com.pulse.member.controller;

import com.pulse.member.config.jwt.JwtTokenProvider;
import com.pulse.member.config.security.http.user.UserDetailsImpl;
import com.pulse.member.controller.request.LoginRequestDTO;
import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberReadRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.entity.RefreshToken;
import com.pulse.member.service.usecase.MemberService;
import com.pulse.member.service.usecase.RefreshTokenService;
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

import java.util.HashMap;
import java.util.Map;

import static com.pulse.member.util.Constant.*;

@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
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

        // 3. JwtTokenProvider를 사용하여 Access Token, Refresh Token을 생성합니다.
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshTokenStr = jwtTokenProvider.generateRefreshToken(authentication);

        // 4. authentication.getPrincipal() 메서드를 호출하여 UserDetailsImpl 객체에 접근합니다.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 5. UserDetailsImpl 객체에서 이메일을 가져와서 사용자 정보를 가져옵니다.
        String email = userDetails.getEmail();
        MemberReadRequestDTO memberDTO = memberService.getMemberByEmail(email);

        // 6. RefreshToken을 생성합니다.
        // todo: 조금 손봐야함 refreshTokenStr을 만들었는데 이걸 안씀
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(memberDTO);

        // 7. 응답 맵을 생성하여 반환합니다.
        Map<String, Object> response = new HashMap<>();
        response.put(ACCESS_TOKEN, accessToken);
        response.put(REFRESH_TOKEN, refreshToken.getToken());
        response.put(EMAIL, userDetails.getEmail());
        response.put(AUTHORITIES, userDetails.getAuthorities());

        return ResponseEntity.ok(response);
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
        // 1. SecurityContextHolder에서 Authentication 객체를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // 2. UserDetailsImpl 객체에서 사용자 정보를 가져옵니다.
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // 3. UserDetailsImpl 객체에서 이메일을 가져와서 사용자 정보를 가져옵니다.
            String email = userDetails.getEmail();
            MemberReadRequestDTO memberDTO = memberService.getMemberByEmail(email);

            // 4. refresh token 삭제
            refreshTokenService.deleteByMember(memberDTO);
        }

        // 5. MemberService의 logout 메서드를 호출하여 사용자를 로그아웃합니다.
        memberService.logout(logoutRequest);
        return ResponseEntity.ok("User logged out successfully");
    }


    /**
     * JWT 토큰 갱신 처리 메서드.
     *
     * @param request 요청 맵
     * @return 갱신된 JWT 토큰
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        // 1. 요청 맵에서 refresh token을 가져옵니다.
        String requestRefreshToken = request.get(REFRESH_TOKEN);

        // 2. refresh token을 찾아서 만료 여부를 확인합니다.
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        refreshTokenService.verifyExpiration(refreshToken);

        // 3. refresh token의 사용자 정보를 가져와서 새로운 access token을 생성합니다.
        String email = refreshToken.getMember().getEmail();
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, null, null);
        String newAccessToken = jwtTokenProvider.generateAccessToken(authenticationToken);

        // 4. 응답 맵을 생성하여 반환합니다.
        Map<String, String> response = new HashMap<>();
        response.put(ACCESS_TOKEN, newAccessToken);
        return ResponseEntity.ok(response);
    }

}
