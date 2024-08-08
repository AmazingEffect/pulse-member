package com.pulse.member.adapter.out.event;

import com.pulse.member.adapter.out.event.outbox.OutboxEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameChangeEvent implements OutboxEvent {

    private Long payload;


    /**
     * @return Long
     * @apiNote 이벤트의 페이로드를 반환한다.
     */
    @Override
    public Long getPayload() {
        return payload;
    }


    /**
     * @return String
     * @apiNote 이벤트의 타입을 반환한다.
     */
    @Override
    public String getEventType() {
        return ExternalEventType.MEMBER_NICKNAME_CHANGE_OUTBOX.getType();
    }

}
