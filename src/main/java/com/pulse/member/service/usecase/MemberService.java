package com.pulse.member.service.usecase;

import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberReadRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;

public interface MemberService {

    MemberSignUpRequestDTO register(MemberSignUpRequestDTO signUpRequest);

    MemberReadRequestDTO getMemberById(Long id);

    MemberReadRequestDTO getMemberByEmail(String email);

    Long changeNickname(Long id, String newNickname);

    void logout(LogoutRequestDTO logoutRequest);

}
