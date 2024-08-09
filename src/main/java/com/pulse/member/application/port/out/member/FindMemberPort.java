package com.pulse.member.application.port.out.member;

import com.pulse.member.domain.Member;

public interface FindMemberPort {

    Member findMemberById(Member member);

    Member findMemberByEmail(String email);

    Member findMemberById(Long memberId);

}
