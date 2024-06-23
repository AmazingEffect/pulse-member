package com.pulse.member.event.spring;

import com.pulse.event_library.event.OutboxEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateEvent implements OutboxEvent {

    private Long id;

    @Override
    public String getEventType() {
        return "MemberCreatedEvent";
    }

}
