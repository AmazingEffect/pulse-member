package com.pulse.member.application.service;

import com.pulse.member.adapter.out.event.outbox.OutboxEvent;
import com.pulse.member.adapter.out.persistence.entity.constant.MessageStatus;
import com.pulse.member.application.port.in.outbox.MemberOutboxUseCase;
import com.pulse.member.application.port.out.outbox.CreateMemberOutboxPort;
import com.pulse.member.application.port.out.outbox.FindMemberOutboxPort;
import com.pulse.member.domain.MemberOutbox;
import io.opentelemetry.api.trace.Span;
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
public class MemberOutboxService implements MemberOutboxUseCase {

    private final CreateMemberOutboxPort createMemberOutboxPort;
    private final FindMemberOutboxPort findMemberOutboxPort;


    /**
     * @param event OutboxEvent
     * @apiNote OutboxEvent를 저장한다. 상태는 PENDING(대기)으로 저장
     */
    @Override
    public Long saveOutboxEvent(OutboxEvent event) {
        // 1. 현재 Span에서 Trace ID를 가져옵니다.
        Span currentSpan = Span.current();
        String nowTraceId = currentSpan.getSpanContext().getTraceId();

        // 2. 이벤트 타입에 따라 적절한 토픽 이름을 반환합니다.
        String eventType = getKafkaTopic(event);

        // 3. MemberOutbox 도메인을 생성합니다.
        MemberOutbox memberOutbox = MemberOutbox.of(eventType, event.getPayload(), nowTraceId, MessageStatus.PENDING);

        // 4. MemberOutbox 도메인을 저장합니다.
        return createMemberOutboxPort.saveMemberOutboxEvent(memberOutbox);
    }


    /**
     * @param event OutboxEvent
     * @apiNote OutboxEvent를 처리대기(PENDING)로 변경
     */
    @Override
    public void markOutboxEventPending(OutboxEvent event) {
        // 1. 이벤트 타입에 따라 적절한 토픽 이름을 반환합니다.
        String eventType = getKafkaTopic(event);

        // 2. 영속화를 위해 OutboxEvent를 찾습니다.
        MemberOutbox memberOutbox = findMemberOutboxPort.findMemberOutboxBy(event.getPayload(), eventType);

        // 3. OutboxEvent의 상태를 PENDING로 변경합니다.
        memberOutbox.changeStatus(MessageStatus.PENDING);
        memberOutbox.changeProcessedAt(LocalDateTime.now());
        createMemberOutboxPort.saveMemberOutboxEvent(memberOutbox);
    }


    /**
     * @param event OutboxEvent
     * @apiNote OutboxEvent를 성공(SUCCESS)로 변경
     * 만약 Feign 요청이 성공해서 데이터를 전달한 후 오류가 없다면 이 메서드를 호출한다.
     */
    @Override
    public void markOutboxEventSuccess(OutboxEvent event) {
        // 1. 이벤트 타입에 따라 적절한 토픽 이름을 반환합니다.
        String eventType = getKafkaTopic(event);

        // 2. 영속화를 위해 OutboxEvent를 찾습니다.
        MemberOutbox memberOutbox = findMemberOutboxPort.findMemberOutboxBy(event.getPayload(), eventType);

        // 3. OutboxEvent의 상태를 SUCCESS로 변경합니다.
        memberOutbox.changeStatus(MessageStatus.PROCESSED);
        memberOutbox.changeProcessedAt(LocalDateTime.now());
        createMemberOutboxPort.saveMemberOutboxEvent(memberOutbox);
    }


    /**
     * @param event OutboxEvent
     * @apiNote OutboxEvent를 실패(FAIL)로 변경
     */
    @Override
    public void markOutboxEventFailed(OutboxEvent event) {
        // 1. 이벤트 타입에 따라 적절한 토픽 이름을 반환합니다.
        String eventType = getKafkaTopic(event);

        // 2. 영속화를 위해 OutboxEvent를 찾습니다.
        MemberOutbox memberOutbox = findMemberOutboxPort.findMemberOutboxBy(event.getPayload(), eventType);

        // 3. OutboxEvent의 상태를 FAIL로 변경합니다.
        memberOutbox.changeStatus(MessageStatus.FAIL);
        memberOutbox.changeProcessedAt(LocalDateTime.now());
        createMemberOutboxPort.saveMemberOutboxEvent(memberOutbox);
    }


    /**
     * @param event OutboxEvent
     * @return String
     * @apiNote OutboxEvent의 이벤트 타입(ENUM)에 따라 적절한 토픽 이름을 반환한다. (kafka topic 으로 사용)
     */
    @Override
    public String getKafkaTopic(OutboxEvent event) {
        return event.getEventType();
    }


    /**
     * @param event OutboxEvent
     * @apiNote OutboxEvent를 처리완료(PROCESSED)로 변경
     */
    @Override
    public void markOutboxEventProcessed(OutboxEvent event) {
        // 1. 이벤트 타입에 따라 적절한 토픽 이름을 반환합니다.
        String eventType = getKafkaTopic(event);

        // 2. 영속화를 위해 OutboxEvent를 찾습니다.
        MemberOutbox memberOutbox = findMemberOutboxPort.findMemberOutboxBy(event.getPayload(), eventType);

        // 3. OutboxEvent의 상태를 PROCESSED로 변경합니다.
        memberOutbox.changeStatus(MessageStatus.PROCESSED);
        memberOutbox.changeProcessedAt(LocalDateTime.now());
        createMemberOutboxPort.saveMemberOutboxEvent(memberOutbox);
    }

}