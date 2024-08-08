package com.pulse.member.adapter.out.event.outbox;

public interface OutboxEvent {

    Long getPayload();

    String getEventType();

}
