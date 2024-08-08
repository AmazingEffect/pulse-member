package com.pulse.member.application.port.in.outbox;

import com.pulse.member.adapter.out.event.outbox.OutboxEvent;

public interface OutboxUseCase {

    void markOutboxEventProcessed(OutboxEvent event);

    void saveOutboxEvent(OutboxEvent event);

    void markOutboxEventSuccess(OutboxEvent event);

    void markOutboxEventFailed(OutboxEvent event);

    String getKafkaTopic(OutboxEvent event);

}
