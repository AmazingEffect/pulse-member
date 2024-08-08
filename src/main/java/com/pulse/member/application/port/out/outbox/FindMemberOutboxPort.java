package com.pulse.member.application.port.out.outbox;

import com.pulse.member.domain.MemberOutbox;

public interface FindMemberOutboxPort {

    MemberOutbox findMemberOutboxBy(Long payload, String eventType);

}
