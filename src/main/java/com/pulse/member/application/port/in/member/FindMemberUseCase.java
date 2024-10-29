package com.pulse.member.application.port.in.member;

import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.application.query.FindMemberQuery;
import com.pulse.member.domain.Member;

public interface FindMemberUseCase {

    MemberResponseDTO findMemberById(Member member);

    MemberResponseDTO findMemberByEmail(Member member);

    MemberResponseDTO findMember(FindMemberQuery command);
    
}
