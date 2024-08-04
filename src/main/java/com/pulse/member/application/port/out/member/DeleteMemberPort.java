package com.pulse.member.application.port.out.member;

import com.pulse.member.domain.Member;

public interface DeleteMemberPort {

    Boolean deleteMemberById(Member member);

}
