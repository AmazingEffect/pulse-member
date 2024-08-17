package com.pulse.member.application.port.in.member;

import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.domain.Member;

public interface CreateMemberUseCase {

    MemberResponseDTO createMember(Member member);

}
