package com.pulse.member.listener.spring.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogEvent {

    private Long id;

    private String action;

    public static Object of(Long id, String action) {
        return new ActivityLogEvent(id, action);
    }

}
