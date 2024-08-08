package com.pulse.member.adapter.out.event;

import com.pulse.member.adapter.out.event.outbox.OutboxEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이벤트 발행은 외부 시스템과의 상호작용 중 하나이므로 adapter > out 이 위치에 이벤트 객체를 두는 것이 일반적이다.
 * 또한 만약 이벤트가 내부에서만 사용된다면, domain 패키지 내에서 정의될 수도 있다.
 * 그러나 외부 시스템과의 통신을 목적으로 한다면 adapter > out > event에 위치하는 것이 더 적절하다.
 * 지금 나는 외부 시스템과의 통신을 목적으로 하기 때문에 adapter > out > event에 위치시킨다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateEvent implements OutboxEvent {

    private Long id;


    @Override
    public Long getPayload() {
        return id;
    }

    @Override
    public String getEventType() {
        return ExternalEventType.MEMBER_CREATE_OUTBOX.getEventType();
    }

}
