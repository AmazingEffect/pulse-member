package com.pulse.member.service.usecase;

import com.pulse.member.controller.request.LoginRequestDTO;
import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.controller.response.JwtResponseDTO;
import com.pulse.member.controller.response.MemberSignUpResponseDTO;

import java.util.Map;

public interface AuthService {

    JwtResponseDTO signInAndMakeJwt(LoginRequestDTO loginRequest);

    MemberSignUpResponseDTO signUpAndPublishEvent(MemberSignUpRequestDTO signUpRequest);

    void signOutAndDeleteJwt(LogoutRequestDTO logoutRequest);

    JwtResponseDTO reIssueRefreshToken(Map<String, String> request);

}
