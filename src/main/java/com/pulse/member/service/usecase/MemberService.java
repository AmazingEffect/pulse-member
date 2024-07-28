package com.pulse.member.service.usecase;

import com.pulse.member.controller.response.MemberReadResponseDTO;

public interface MemberService {

    MemberReadResponseDTO getMemberById(Long id);

    MemberReadResponseDTO getMemberByEmail(String email);

    Long changeNickname(Long id, String newNickname);

}
