package com.pulse.member.application.port.in;

import com.pulse.member.adapter.in.web.dto.response.MemberReadResponseDTO;

public interface MemberUseCase {

    MemberReadResponseDTO getMemberById(Long id);

    MemberReadResponseDTO getMemberByEmail(String email);

    Long changeNickname(Long id, String newNickname);

}
