package com.pulse.member.adapter.out.event;

import com.pulse.event_library.event.OutboxEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImageChangeEvent implements OutboxEvent {

    private Long id;

    @Override
    public String getEventType() {
        return ExternalEventType.MEMBER_PROFILE_IMAGE_CHANGE_OUTBOX.getEventType();
    }

}
