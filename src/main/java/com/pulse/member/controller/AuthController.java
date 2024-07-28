package com.pulse.member.controller;

import com.pulse.member.controller.request.LoginRequestDTO;
import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.controller.response.ApiResponse;
import com.pulse.member.controller.response.JwtResponseDTO;
import com.pulse.member.controller.response.MemberSignUpResponseDTO;
import com.pulse.member.service.usecase.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 회원 인증 관련 API 컨트롤러
 */
@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인 요청을 받아 JWT 토큰을 발급합니다.
     *
     * @param loginRequest 로그인 요청 DTO
     * @return JWT 토큰 응답 DTO
     */
    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> signInAndMakeJwt(
            @RequestBody LoginRequestDTO loginRequest
    ) {
        JwtResponseDTO response = authService.signInAndMakeJwt(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    /**
     * 회원가입 요청을 받아 회원가입을 진행합니다.
     *
     * @param signUpRequest 회원가입 요청 DTO
     * @return 회원가입 응답 DTO
     */
    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<MemberSignUpResponseDTO>> signUpAndPublishEvent(
            @RequestBody MemberSignUpRequestDTO signUpRequest
    ) {
        MemberSignUpResponseDTO dto = authService.signUpAndPublishEvent(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }


    /**
     * 로그아웃 요청을 받아 JWT 토큰을 삭제합니다.
     *
     * @param logoutRequest 로그아웃 요청 DTO
     * @return 로그아웃 성공 메시지
     */
    @PostMapping("/signOut")
    public ResponseEntity<?> signOutAndDeleteJwt(
            @RequestBody LogoutRequestDTO logoutRequest
    ) {
        authService.signOutAndDeleteJwt(logoutRequest);
        return ResponseEntity.ok("User logged out successfully");
    }


    /**
     * Refresh Token을 이용하여 새로운 JWT 토큰을 발급합니다.
     *
     * @param request Refresh Token 요청 DTO
     * @return JWT 토큰 응답 DTO
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> reIssueRefreshToken(
            @RequestBody Map<String, String> request
    ) {
        JwtResponseDTO response = authService.reIssueRefreshToken(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
