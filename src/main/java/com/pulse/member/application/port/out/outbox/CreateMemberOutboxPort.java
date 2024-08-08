package com.pulse.member.application.port.out.outbox;

import com.pulse.member.domain.MemberOutbox;

public interface CreateMemberOutboxPort {

    Long saveMemberOutboxEvent(MemberOutbox outbox);

}
