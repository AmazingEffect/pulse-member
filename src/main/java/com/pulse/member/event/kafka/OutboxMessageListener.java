package com.pulse.member.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.event_library.event.OutboxEvent;
import com.pulse.event_library.service.OutboxService;
import com.pulse.member.event.spring.MemberCreateEvent;
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
public class OutboxMessageListener {

    private final OutboxService outboxService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Kafka 외부 리스너
     * 다른 서버에서 데이터 동기화를 위해 Event를 발행하면 이 리스너가 동작해서 Kafka의 메시지를 수신한다.
     */
    @KafkaListener(
            topics = "ContentCreatedEvent",
            groupId = "member-group-external",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenExternal(
            ConsumerRecord<String, String> record, Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        try {
            log.info("Received message: {} from partition: {} with offset: {}", record.value(), partition, offset);

        } catch (Exception e) {
            throw e;
        }

        acknowledgment.acknowledge();
    }

    /**
     * Kafka 내부 리스너
     * Outbox 테이블에 message_status와 processed_at 컬럼을 업데이트한다.
     */
    @KafkaListener(
            topics = {"member-created-outbox"},
            groupId = "member-group-internal-outbox",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenInternal(
            ConsumerRecord<String, String> record, Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) throws JsonProcessingException {
        try {
            log.info("Received message: {} from partition: {} with offset: {}", record.value(), partition, offset);

            // JSON 데이터를 OutboxEvent로 변환한다.
            String jsonValue = record.value();
            OutboxEvent event = objectMapper.readValue(jsonValue, MemberCreateEvent.class);

            // OutboxService의 markOutboxEventProcessed 메서드를 호출하여 OutboxEvent를 처리완료(PROCESSED)로 변경한다.
            outboxService.markOutboxEventProcessed(event);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            // JSON 변환 중 오류가 발생한 경우 처리
            System.err.println("Error while converting record value to OutboxEvent: " + e.getMessage());
            throw e;
        }
    }

}
