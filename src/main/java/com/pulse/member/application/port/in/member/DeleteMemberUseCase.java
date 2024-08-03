package com.pulse.member.application.port.in.member;

import com.pulse.member.domain.Member;

public interface DeleteMemberUseCase {

    Boolean deleteMemberById(Member member);

    Boolean deleteMemberByEmail(Member member);

}
