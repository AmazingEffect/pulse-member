package com.pulse.member.application.port.in;

import com.pulse.member.adapter.in.web.dto.request.LoginRequestDTO;
import com.pulse.member.adapter.in.web.dto.request.LogoutRequestDTO;
import com.pulse.member.adapter.in.web.dto.request.MemberSignUpRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberSignUpResponseDTO;

import java.util.Map;

public interface AuthUseCase {

    JwtResponseDTO signInAndMakeJwt(LoginRequestDTO loginRequest);

    MemberSignUpResponseDTO signUpAndPublishEvent(MemberSignUpRequestDTO signUpRequest);

    void signOutAndDeleteJwt(LogoutRequestDTO logoutRequest);

    JwtResponseDTO reIssueRefreshToken(Map<String, String> request);

}
