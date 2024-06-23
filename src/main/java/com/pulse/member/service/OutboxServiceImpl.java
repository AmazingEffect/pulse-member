package com.pulse.member.service;

import com.pulse.event_library.event.OutboxEvent;
import com.pulse.event_library.service.OutboxService;
import com.pulse.member.entity.MemberOutbox;
import com.pulse.member.entity.constant.MessageStatus;
import com.pulse.member.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 이벤트 발행여부를 핸들링하는 OutboxService의 구현체
 */
@RequiredArgsConstructor
@Service
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;

    @Override
    public void saveOutboxEvent(OutboxEvent event) {
        MemberOutbox outbox = MemberOutbox.builder()
                .eventType(event.getEventType())
                .eventId(event.getId())
                .status(MessageStatus.PENDING)
                .build();
        outboxRepository.save(outbox);
    }

    @Override
    public Long getOutboxId(OutboxEvent event) {
        MemberOutbox outbox = outboxRepository.findByEventIdAndEventType(event.getId(), event.getEventType())
                .orElseThrow(() -> new IllegalArgumentException("OutboxEvent not found"));

        return outbox != null ? outbox.getId() : null;
    }

    @Override
    public void markOutboxEventProcessed(OutboxEvent event) {
        MemberOutbox outbox = outboxRepository.findByEventIdAndEventType(event.getId(), event.getEventType())
                .orElseThrow(() -> new IllegalArgumentException("OutboxEvent not found"));

        if (outbox != null) {
            outbox.changeStatus(MessageStatus.PROCESSED);
            outbox.changeProcessedAt(LocalDateTime.now());
            outboxRepository.save(outbox);
        }
    }

    @Override
    public void markOutboxEventFailed(OutboxEvent event) {
        MemberOutbox outbox = outboxRepository.findByEventIdAndEventType(event.getId(), event.getEventType())
                .orElseThrow(() -> new IllegalArgumentException("OutboxEvent not found"));

        if (outbox != null) {
            outbox.changeStatus(MessageStatus.FAIL);
            outboxRepository.save(outbox);
        }
    }

    @Override
    public String getKafkaTopic(OutboxEvent event) {
        // 이벤트 타입이나 기타 조건에 따라 적절한 토픽 이름을 반환
        switch (event.getEventType()) {
            case "MemberCreatedEvent":
                return "member-created-topic";
            case "MemberUpdatedEvent":
                return "member-updated-topic";
            case "MemberDeletedEvent":
                return "member-deleted-topic";
            default:
                return "default-topic";
        }
    }

}