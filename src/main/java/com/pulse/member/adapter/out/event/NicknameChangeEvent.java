package com.pulse.member.adapter.out.event;

import com.pulse.event_library.event.OutboxEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameChangeEvent implements OutboxEvent {

    private Long id;

    @Override
    public String getEventType() {
        return ExternalEventType.MEMBER_NICKNAME_CHANGE_OUTBOX.getEventType();
    }

}
