package com.pulse.member.application.port.in.member;

import com.pulse.member.domain.Member;

public interface FindMemberUseCase {

    Member findMemberById(Member member);

    Member findMemberByEmail(Member member);

}
