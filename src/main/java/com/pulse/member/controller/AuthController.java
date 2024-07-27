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

@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> signIn(@RequestBody LoginRequestDTO loginRequest) {
        JwtResponseDTO response = authService.signIn(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<MemberSignUpResponseDTO>> signUp(@RequestBody MemberSignUpRequestDTO signUpRequest) {
        MemberSignUpResponseDTO dto = authService.signUp(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }


    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(@RequestBody LogoutRequestDTO logoutRequest) {
        authService.signOut(logoutRequest);
        return ResponseEntity.ok("User logged out successfully");
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> refreshToken(@RequestBody Map<String, String> request) {
        JwtResponseDTO response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
