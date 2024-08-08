package com.pulse.member.application.port.in.member;

import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.application.command.member.FindMemberCommand;
import com.pulse.member.domain.Member;

public interface FindMemberUseCase {

    Member findMemberById(Member member);

    Member findMemberByEmail(Member member);

    MemberResponseDTO findMember(FindMemberCommand command);
    
}
