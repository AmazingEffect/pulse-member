package com.pulse.member.adapter.out.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 내부 이벤트 타입을 정의하는 Enum 클래스
 * spring event 에서 사용되는 이벤트 타입을 정의한다.
 */
@Getter
@AllArgsConstructor
public enum InternalEventType {

    ACTIVITY_LOG("ActivityLogEvent", "활동 로그 이벤트"),

    ;

    private final String eventType;
    private final String eventDescription;

}
