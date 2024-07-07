package com.pulse.member.listener.kafka.outbox.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.event_library.event.OutboxEvent;
import com.pulse.event_library.service.OutboxService;
import com.pulse.member.config.trace.annotation.TraceOutboxKafka;
import com.pulse.member.listener.spring.event.MemberCreateEvent;
import com.pulse.member.util.TraceUtil;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * 서버간 데이터 전송: Outbox 관련 Kafka 리스너
 *
 * @KafkaListener 어노테이션을 사용하면 동일한 그룹 ID를 사용하더라도 각 리스너는 지정된 토픽에 따라 독립적으로 동작한다. (여러 서버에서 동시 호출)
 * Kafka의 동작 방식에 따라, 동일한 토픽을 구독하지만 다른 groupId를 가진 컨슈머 그룹은 동일한 메시지를 각각의 그룹에서 수신하고 처리합니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberCreateMessageListener {

    private final OutboxService outboxService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Kafka 내부 리스너
     * Outbox 테이블에 message_status와 processed_at 컬럼을 업데이트한다.
     */
    @TraceOutboxKafka
    @KafkaListener(
            topics = {"member-created-outbox"},
            groupId = "member-group-member-create",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenInternal(
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) throws JsonProcessingException {
        // 1. record 값을 이벤트 객체로 변환한다.
        String jsonValue = record.value();
        OutboxEvent event = objectMapper.readValue(jsonValue, MemberCreateEvent.class);

        // 2. outbox 테이블에 이벤트 처리 상태를 업데이트한다.
        outboxService.markOutboxEventProcessed(event);

        // 3. ack 처리
        acknowledgment.acknowledge();
    }

}
