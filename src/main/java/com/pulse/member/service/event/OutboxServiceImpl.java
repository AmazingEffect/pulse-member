package com.pulse.member.service.event;

import com.pulse.event_library.event.OutboxEvent;
import com.pulse.event_library.service.OutboxService;
import com.pulse.member.entity.MemberOutbox;
import com.pulse.member.entity.constant.MessageStatus;
import com.pulse.member.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 이벤트 발행여부를 핸들링하는 OutboxService의 구현체
 */
@Transactional
@RequiredArgsConstructor
@Service
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;

    /**
     * OutboxEvent를 저장한다.
     * 상태는 PENDING(대기)으로 저장
     *
     * @param event
     */
    @Override
    public void saveOutboxEvent(OutboxEvent event) {
        String eventType = getKafkaTopic(event);
        MemberOutbox outbox = MemberOutbox.builder()
                .eventType(eventType)
                .eventId(event.getId())
                .status(MessageStatus.PENDING)
                .build();
        outboxRepository.save(outbox);
    }

    /**
     * OutboxEvent를 처리완료(PROCESSED)로 변경
     *
     * @param event
     */
    @Override
    public void markOutboxEventProcessed(OutboxEvent event) {
        String eventType = getKafkaTopic(event);
        MemberOutbox outbox = outboxRepository.findByEventIdAndEventType(event.getId(), eventType)
                .orElseThrow(() -> new IllegalArgumentException("OutboxEvent not found"));

        if (outbox != null) {
            outbox.changeStatus(MessageStatus.PROCESSED);
            outbox.changeProcessedAt(LocalDateTime.now());
            outboxRepository.save(outbox);
        }
    }

    /**
     * OutboxEvent를 성공(SUCCESS)로 변경
     * 만약 Feign 요청이 성공해서 데이터를 전달한 후 오류가 없다면 이 메서드를 호출한다.
     *
     * @param event
     */
    @Override
    public void markOutboxEventSuccess(OutboxEvent event) {
        String eventType = getKafkaTopic(event);
        MemberOutbox outbox = outboxRepository.findByEventIdAndEventType(event.getId(), eventType)
                .orElseThrow(() -> new IllegalArgumentException("OutboxEvent not found"));

        if (outbox != null) {
            outbox.changeStatus(MessageStatus.SUCCESS);
            outboxRepository.save(outbox);
        }
    }

    /**
     * OutboxEvent를 실패(FAIL)로 변경
     *
     * @param event
     */
    @Override
    public void markOutboxEventFailed(OutboxEvent event) {
        String eventType = getKafkaTopic(event);
        MemberOutbox outbox = outboxRepository.findByEventIdAndEventType(event.getId(), eventType)
                .orElseThrow(() -> new IllegalArgumentException("OutboxEvent not found"));

        if (outbox != null) {
            outbox.changeStatus(MessageStatus.FAIL);
            outboxRepository.save(outbox);
        }
    }

    /**
     * OutboxEvent의 Kafka 토픽을 반환
     * getType() 메서드로 꺼낸 이벤트 타입에 따라 적절한 토픽 이름을 반환한다.
     *
     * @param event
     * @return
     */
    @Override
    public String getKafkaTopic(OutboxEvent event) {
        // 이벤트 타입이나 기타 조건에 따라 적절한 토픽 이름을 반환
        return switch (event.getEventType()) {
            case "MemberCreatedOutboxEvent" -> "member-created-outbox";
            case "MemberUpdatedOutboxEvent" -> "member-updated-outbox";
            case "MemberDeletedOutboxEvent" -> "member-deleted-outbox";
            default -> "default-topic";
        };
    }

}