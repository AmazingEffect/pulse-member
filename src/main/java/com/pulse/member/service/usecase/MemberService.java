package com.pulse.member.service.usecase;

import com.pulse.member.dto.MemberDTO;

public interface MemberService {

    MemberDTO getMemberById(Long id);

    MemberDTO createMember(MemberDTO memberDTO);

}
