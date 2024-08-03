package com.pulse.member.domain;

import com.pulse.member.adapter.out.persistence.entity.constant.MessageStatus;
import lombok.*;

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

}