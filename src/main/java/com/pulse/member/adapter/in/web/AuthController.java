package com.pulse.member.adapter.in.web;

import com.pulse.member.adapter.in.web.dto.request.MemberSignUpRequestDTO;
import com.pulse.member.adapter.in.web.dto.request.ReIssueAccessTokenDTO;
import com.pulse.member.adapter.in.web.dto.request.SignInRequestDTO;
import com.pulse.member.adapter.in.web.dto.request.SignOutRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.api.ApiResponse;
import com.pulse.member.application.command.auth.ReIssueAccessTokenCommand;
import com.pulse.member.application.command.auth.SignInCommand;
import com.pulse.member.application.command.auth.SignOutCommand;
import com.pulse.member.application.command.auth.SignUpCommand;
import com.pulse.member.application.port.in.auth.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 인증 관련 API 컨트롤러
 */
@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthUseCase authUseCase;


    /**
     * @param signUpRequestDTO 회원가입 요청 DTO
     * @return 회원가입 응답 DTO
     * @apiNote 회원가입 요청을 받아 회원가입을 진행합니다.
     */
    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> signUpAndPublishEvent(
            @RequestBody MemberSignUpRequestDTO signUpRequestDTO
    ) {
        SignUpCommand signUpCommand = SignUpCommand.of(signUpRequestDTO);
        MemberResponseDTO responseDTO = authUseCase.signUp(signUpCommand);

        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }


    /**
     * @param signInRequestDTO 로그인 요청 DTO
     * @return JWT 토큰 응답 DTO
     * @apiNote 로그인 요청을 받아 JWT 토큰을 발급합니다.
     */
    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> signInAndMakeJwt(
            @RequestBody SignInRequestDTO signInRequestDTO
    ) {
        SignInCommand signInCommand = SignInCommand.of(signInRequestDTO);
        JwtResponseDTO responseDTO = authUseCase.signInAndPublishJwt(signInCommand);

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
        SignOutCommand signOutCommand = SignOutCommand.of(signOutRequestDTO);
        Long response = authUseCase.signOut(signOutCommand);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    /**
     * @param reIssueAccessTokenDTO Jwt access token 재발급 요청 DTO
     * @return JWT 토큰 응답 DTO
     * @apiNote Refresh Token을 이용하여 새로운 JWT 토큰을 발급합니다.
     */
    @PostMapping("/reIssueAccessToken")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> reIssueAccessToken(
            @RequestBody ReIssueAccessTokenDTO reIssueAccessTokenDTO
    ) {
        ReIssueAccessTokenCommand reIssueAccessTokenCommand = ReIssueAccessTokenCommand.of(reIssueAccessTokenDTO);
        JwtResponseDTO responseDTO = authUseCase.reIssueAccessToken(reIssueAccessTokenCommand);

        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

}
