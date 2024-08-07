package com.pulse.member.adapter.in.web;

import com.pulse.member.adapter.in.web.dto.request.MemberSignUpRequestDTO;
import com.pulse.member.adapter.in.web.dto.request.SignInRequestDTO;
import com.pulse.member.adapter.in.web.dto.request.SignOutRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.ApiResponse;
import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberSignUpResponseDTO;
import com.pulse.member.application.command.SignInCommand;
import com.pulse.member.application.command.SignOutCommand;
import com.pulse.member.application.command.SignUpCommand;
import com.pulse.member.application.port.in.auth.AuthUseCase;
import com.pulse.member.exception.ErrorCode;
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

    private final AuthUseCase authUseCase;


    /**
     * @param signInRequestDTO 로그인 요청 DTO
     * @return JWT 토큰 응답 DTO
     * @apiNote 로그인 요청을 받아 JWT 토큰을 발급합니다.
     */
    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> signInAndMakeJwt(
            @RequestBody SignInRequestDTO signInRequestDTO
    ) {
        // requestDTO를 command로 변환
        SignInCommand signInCommand = SignInCommand.of(signInRequestDTO);

        // useCase는 command를 받아서 responseDTO를 반환
        JwtResponseDTO responseDTO = authUseCase.signInAndPublishJwt(signInCommand);

        if (responseDTO == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }


    /**
     * @param signUpRequestDTO 회원가입 요청 DTO
     * @return 회원가입 응답 DTO
     * @apiNote 회원가입 요청을 받아 회원가입을 진행합니다.
     */
    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<MemberSignUpResponseDTO>> signUpAndPublishEvent(
            @RequestBody MemberSignUpRequestDTO signUpRequestDTO
    ) {
        // requestDTO를 command로 변환
        SignUpCommand signUpCommand = SignUpCommand.of(signUpRequestDTO);

        // useCase는 command를 받아서 responseDTO를 반환
        MemberSignUpResponseDTO responseDTO = authUseCase.signUp(signUpCommand);

        if (responseDTO == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }


    /**
     * @param signOutRequestDTO 로그아웃 요청 DTO
     * @return 로그아웃 성공 메시지
     * @apiNote 로그아웃 요청을 받아 JWT 토큰을 삭제합니다.
     */
    @PostMapping("/signOut")
    public ResponseEntity<ApiResponse<Long>> signOutAndDeleteJwt(
            @RequestBody SignOutRequestDTO signOutRequestDTO
    ) {
        // requestDTO를 command로 변환
        SignOutCommand signOutCommand = SignOutCommand.of(signOutRequestDTO);

        // useCase는 command를 받아서 responseDTO를 반환
        Long response = authUseCase.signOut(signOutCommand);

        if (response == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    /**
     * @param request Refresh Token 요청 DTO
     * @return JWT 토큰 응답 DTO
     * @apiNote Refresh Token을 이용하여 새로운 JWT 토큰을 발급합니다.
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> reIssueRefreshToken(
            @RequestBody Map<String, String> request
    ) {
        JwtResponseDTO responseDTO = authUseCase.reIssueRefreshToken(request);

        if (responseDTO == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

}
