package com.pulse.member.application.port.in.outbox;

import com.pulse.member.adapter.out.event.outbox.OutboxEvent;

public interface MemberOutboxUseCase {

    void markOutboxEventPending(OutboxEvent event);

    Long saveOutboxEvent(OutboxEvent event);

    void markOutboxEventSuccess(OutboxEvent event);

    void markOutboxEventFailed(OutboxEvent event);

    String getKafkaTopic(OutboxEvent event);

    void markOutboxEventProcessed(OutboxEvent event);

}
