package com.pulse.member.application.port.out.outbox;

import com.pulse.member.domain.MemberOutbox;

public interface FindOutboxPort {

    MemberOutbox findOutboxBy(Long payload, String eventType);

}
