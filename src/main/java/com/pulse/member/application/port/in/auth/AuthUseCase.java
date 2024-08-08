package com.pulse.member.application.port.in.auth;

import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.application.command.auth.SignInCommand;
import com.pulse.member.application.command.auth.SignOutCommand;
import com.pulse.member.application.command.auth.SignUpCommand;

import java.util.Map;

/**
 * useCase에서는 Command를 받아서 도메인을 반환한다.
 */
public interface AuthUseCase {

    JwtResponseDTO signInAndPublishJwt(SignInCommand signInCommand);

    MemberResponseDTO signUp(SignUpCommand signUpCommand);

    Long signOut(SignOutCommand signOutCommand);

    JwtResponseDTO reIssueRefreshToken(Map<String, String> request);

}
