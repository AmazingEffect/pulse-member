package com.pulse.member.application.port.in.member;

import com.pulse.member.domain.Member;

public interface UpdateMemberUseCase {

    Member updateMemberById(Member member);

    Member updateMemberByEmail(Member member);

}
