package com.pulse.member.domain;

import com.pulse.member.adapter.out.persistence.entity.constant.MessageStatus;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * 이벤트 발행 및 Kafka 메시지 송/수신을 관리하기 위한 Outbox
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberOutbox {

    private Long id;
    private String eventType;   // 토픽정보 ex.MemberCreatedEvent
    private Long payload;       // 이벤트 내부의 id 필드를 저장. ex) memberId: 1L
    private String traceId;     // Kafka 메시지 처리 시, traceId
    private MessageStatus status;      // Kafka 메시지 처리 상태 (예: PENDING, PROCESSED, SUCCESS, FAIL)
    private LocalDateTime processedAt; // Kafka 메시지 처리 시간 (처리된 경우)


    // factory method
    public static MemberOutbox of(String eventType, Long payload, String nowTraceId, MessageStatus messageStatus) {
        return MemberOutbox.builder()
                .eventType(eventType)
                .payload(payload)
                .traceId(nowTraceId)
                .status(messageStatus)
                .build();
    }


    /**
     * @param messageStatus 메시지 상태
     * @apiNote OutboxEvent의 상태를 변경
     */
    public void changeStatus(MessageStatus messageStatus) {
        if (ObjectUtils.isEmpty(messageStatus)) {
            throw new MemberException(ErrorCode.OUTBOX_STATUS_NOT_FOUND);
        }
        this.status = messageStatus;
    }


    /**
     * @param now LocalDateTime
     * @apiNote OutboxEvent를 처리완료(PROCESSED)로 변경
     */
    public void changeProcessedAt(LocalDateTime now) {
        if (ObjectUtils.isEmpty(now)) {
            throw new MemberException(ErrorCode.OUTBOX_PROCESSED_AT_NOT_FOUND);
        }
        this.processedAt = now;
    }

}