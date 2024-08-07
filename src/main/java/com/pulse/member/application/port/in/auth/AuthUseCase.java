package com.pulse.member.application.port.in.auth;

import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberSignUpResponseDTO;
import com.pulse.member.application.command.SignInCommand;
import com.pulse.member.application.command.SignOutCommand;
import com.pulse.member.application.command.SignUpCommand;

import java.util.Map;

/**
 * useCase에서는 Command를 받아서 도메인을 반환한다.
 */
public interface AuthUseCase {

    JwtResponseDTO signInAndPublishJwt(SignInCommand signInCommand);

    MemberSignUpResponseDTO signUp(SignUpCommand signUpCommand);

    Long signOut(SignOutCommand signOutCommand);

    JwtResponseDTO reIssueRefreshToken(Map<String, String> request);

}
