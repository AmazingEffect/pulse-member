package com.pulse.member.service.usecase;

import com.pulse.member.controller.request.LoginRequestDTO;
import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.controller.response.JwtResponseDTO;
import com.pulse.member.controller.response.MemberSignUpResponseDTO;

import java.util.Map;

public interface AuthService {

    JwtResponseDTO signIn(LoginRequestDTO loginRequest);

    MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO signUpRequest);

    void signOut(LogoutRequestDTO logoutRequest);

    JwtResponseDTO refreshToken(Map<String, String> request);

}
