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
import com.pulse.member.domain.Member;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.mapper.JwtMapper;
import com.pulse.member.mapper.MemberMapper;
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
    private final JwtMapper jwtMapper;
    private final MemberMapper memberMapper;

    /**
     * 로그인 요청을 받아 JWT 토큰을 발급합니다.
     *
     * @param loginRequest 로그인 요청 DTO
     * @return JWT 토큰 응답 DTO
     */
    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<JwtResponseDTO>> signInAndMakeJwt(
            @RequestBody SignInRequestDTO signInRequestDTO
    ) {
        // requestDTO를 command로 변환
        SignInCommand signInCommand = SignInCommand.of(signInRequestDTO);

        // useCase는 command를 받아서 responseDTO를 반환
        Member responseMember = authUseCase.signInAndPublishJwt(signInCommand);
        JwtResponseDTO jwtResponseDTO = jwtMapper.toResponseDTO(responseMember.getJwt());

        if (jwtResponseDTO == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.success(jwtResponseDTO));
    }


    /**
     * 회원가입 요청을 받아 회원가입을 진행합니다.
     *
     * @param signUpRequest 회원가입 요청 DTO
     * @return 회원가입 응답 DTO
     */
    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<MemberSignUpResponseDTO>> signUpAndPublishEvent(
            @RequestBody MemberSignUpRequestDTO signUpRequestDTO
    ) {
        // requestDTO를 command로 변환
        SignUpCommand signUpCommand = SignUpCommand.of(signUpRequestDTO);

        // useCase는 command를 받아서 responseDTO를 반환
        Member responseMember = authUseCase.signUp(signUpCommand);
        MemberSignUpResponseDTO responseDTO = memberMapper.toResponseDTO(responseMember);

        if (responseDTO == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }


    /**
     * 로그아웃 요청을 받아 JWT 토큰을 삭제합니다.
     *
     * @param logoutRequest 로그아웃 요청 DTO
     * @return 로그아웃 성공 메시지
     */
    @PostMapping("/signOut")
    public ResponseEntity<?> signOutAndDeleteJwt(
            @RequestBody SignOutRequestDTO signOutRequestDTO
    ) {
        // requestDTO를 command로 변환
        SignOutCommand signOutCommand = SignOutCommand.of(signOutRequestDTO);

        // useCase는 command를 받아서 responseDTO를 반환
        Long signOutResponse = authUseCase.signOut(signOutCommand);

        if (signOutResponse == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
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
        JwtResponseDTO responseDTO = authUseCase.reIssueRefreshToken(request);

        if (responseDTO == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

}
