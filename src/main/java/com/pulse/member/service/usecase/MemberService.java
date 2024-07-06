package com.pulse.member.service.usecase;

import com.pulse.member.dto.MemberCreateDTO;
import com.pulse.member.dto.MemberRetrieveDTO;

public interface MemberService {

    MemberRetrieveDTO getMemberById(Long id);

    MemberCreateDTO createMember(MemberCreateDTO memberCreateDTO);

    Long changeNickname(Long id, String newNickname);

}
