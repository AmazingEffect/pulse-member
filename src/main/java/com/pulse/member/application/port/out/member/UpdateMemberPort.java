package com.pulse.member.application.port.out.member;

import com.pulse.member.domain.Member;

public interface UpdateMemberPort {

    Member updateMemberById(Member member);

    Member updateMemberByEmail(Member member);

}
