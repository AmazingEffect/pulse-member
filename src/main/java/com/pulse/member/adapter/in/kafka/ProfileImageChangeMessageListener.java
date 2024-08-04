package com.pulse.member.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.event_library.event.OutboxEvent;
import com.pulse.event_library.service.OutboxService;
import com.pulse.member.adapter.out.event.MemberCreateEvent;
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
 * 프로필 이미지가 변경되면 발행되는 kafka 메시지를 처리하는 리스너
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileImageChangeMessageListener {

    private final OutboxService outboxService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("kafka-consumer");
    private final TraceUtil traceUtil;

    /**
     * Kafka 내부 리스너
     * Outbox 테이블에 message_status와 processed_at 컬럼을 업데이트한다.
     */
    @KafkaListener(
            topics = {"member-profile-image-change-outbox"},
            groupId = "member-group-profile-image-change",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenInternal(
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) throws JsonProcessingException {
        // 1. Span을 생성한다. (분산 추적 설정)
        Span span = getSpan(record, partition, offset);

        // 2. Span을 현재 컨텍스트에 설정하고 메시지를 처리한다.
        try (Scope scope = span.makeCurrent()) {
            String jsonValue = record.value();
            OutboxEvent event = objectMapper.readValue(jsonValue, MemberCreateEvent.class);
            outboxService.markOutboxEventProcessed(event);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            span.recordException(e);
            log.error("Error occurred while processing message: {}", e.getMessage());
            throw e;
        } finally {
            // 3. Span을 종료한다.
            span.end();
        }

        // 4. 메시지 처리 완료 후 ack를 보낸다.
        acknowledgment.acknowledge();
    }

    /**
     * Kafka header에 있는 traceparent를 추출하여 Span을 생성한다.
     *
     * @param record    Kafka 메시지
     * @param partition 파티션
     * @param offset    오프셋
     * @return Span
     */
    private Span getSpan(ConsumerRecord<String, String> record, int partition, long offset) {
        // 1. Kafka 메시지에서 traceparent 헤더를 추출하여 SpanContext를 생성한다.
        Context extractedContext = traceUtil.extractContextFromRecord(record);

        // 2. Span을 생성한다.
        Span span = tracer.spanBuilder("KafkaListener Process Message - Member")
                .setAttribute("partition", partition)
                .setAttribute("offset", offset)
                .setParent(extractedContext)
                .startSpan();

        return span;
    }

}
