package com.pulse.member.service.usecase;

import com.pulse.member.controller.request.LoginRequestDTO;
import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.controller.response.JwtResponseDTO;
import com.pulse.member.controller.response.MemberReadResponseDTO;
import com.pulse.member.controller.response.MemberSignUpResponseDTO;
import com.pulse.member.entity.RefreshToken;

import java.util.Map;

public interface AuthService {

    JwtResponseDTO signInAndMakeJwt(LoginRequestDTO loginRequest);

    MemberSignUpResponseDTO signUpAndPublishEvent(MemberSignUpRequestDTO signUpRequest);

    void signOutAndDeleteJwt(LogoutRequestDTO logoutRequest);

    RefreshToken createRefreshToken(MemberReadResponseDTO memberReadResponseDTO);

    JwtResponseDTO reIssueRefreshToken(Map<String, String> request);

    RefreshToken findByToken(String token);

    void deleteByMember(MemberReadResponseDTO memberReadResponseDTO);

    void verifyExpiration(RefreshToken token);

}
