package com.pulse.member.service.usecase;

import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.dto.MemberCreateDTO;
import com.pulse.member.dto.MemberRetrieveDTO;

public interface MemberService {

    MemberSignUpRequestDTO register(MemberSignUpRequestDTO signUpRequest);

    MemberRetrieveDTO getMemberById(Long id);

    Long changeNickname(Long id, String newNickname);

    void logout(LogoutRequestDTO logoutRequest);

}
