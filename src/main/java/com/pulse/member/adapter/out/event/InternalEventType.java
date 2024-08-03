package com.pulse.member.adapter.out.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalEventType {

    ACTIVITY_LOG("ActivityLogEvent", "활동 로그 이벤트"),

    ;

    private final String eventType;
    private final String eventDescription;

}
