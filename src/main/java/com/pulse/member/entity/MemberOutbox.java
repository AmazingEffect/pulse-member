package com.pulse.member.entity;

import com.pulse.member.entity.constant.MessageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 이벤트 발행 및 Kafka 메시지 송/수신을 관리하기 위한 Outbox 엔티티
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "member_outbox")
public class MemberOutbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType;   // 토픽정보 ex.MemberCreatedEvent

    @Column(name = "payload", nullable = false)
    private Long payload;       // 이벤트 내부의 id 필드를 저장. ex) memberId: 1L

    @Column(name = "trace_id", nullable = false)
    private String traceId;     // Kafka 메시지 처리 시, traceId

    @Enumerated(EnumType.STRING)
    @Column(name = "message_status", nullable = false)
    private MessageStatus status;      // Kafka 메시지 처리 상태 (예: PENDING, PROCESSED, SUCCESS, FAIL)

    @Column(name = "processed_at")
    private LocalDateTime processedAt; // Kafka 메시지 처리 시간 (처리된 경우)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberOutbox that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public void changeStatus(MessageStatus messageStatus) {
        this.status = messageStatus;
    }

    public void changeProcessedAt(LocalDateTime now) {
        this.processedAt = now;
    }

}