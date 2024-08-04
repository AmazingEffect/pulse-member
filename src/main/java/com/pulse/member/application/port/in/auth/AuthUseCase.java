package com.pulse.member.application.port.in.auth;

import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.domain.Member;

import java.util.Map;

public interface AuthUseCase {

    Member signIn(Member member);

    Member signUp(Member member);

    void signOut(Member member);

    JwtResponseDTO reIssueRefreshToken(Map<String, String> request);

}
