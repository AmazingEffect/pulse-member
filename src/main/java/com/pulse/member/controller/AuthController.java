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

    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> signInAndMakeJwt(
            @RequestBody LoginRequestDTO loginRequest
    ) {
        JwtResponseDTO response = authService.signInAndMakeJwt(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<MemberSignUpResponseDTO>> signUpAndPublishEvent(
            @RequestBody MemberSignUpRequestDTO signUpRequest
    ) {
        MemberSignUpResponseDTO dto = authService.signUpAndPublishEvent(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }


    @PostMapping("/signOut")
    public ResponseEntity<?> signOutAndDeleteJwt(
            @RequestBody LogoutRequestDTO logoutRequest
    ) {
        authService.signOutAndDeleteJwt(logoutRequest);
        return ResponseEntity.ok("User logged out successfully");
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> reIssueRefreshToken(
            @RequestBody Map<String, String> request
    ) {
        JwtResponseDTO response = authService.reIssueRefreshToken(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
