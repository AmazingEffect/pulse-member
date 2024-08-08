package com.pulse.member.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.member.adapter.out.event.MemberCreateEvent;
import com.pulse.member.adapter.out.event.NicknameChangeEvent;
import com.pulse.member.adapter.out.event.ProfileImageChangeEvent;
import com.pulse.member.adapter.out.event.outbox.OutboxEvent;
import com.pulse.member.application.port.in.outbox.MemberOutboxUseCase;
import com.pulse.member.config.trace.annotation.TraceOutboxKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * Outbox 테이블의 상태를 업데이트하는 리스너 (Transactional Outbox Pattern 내부 Kafka 리스너)
 * Outbox 테이블에 message_status와 processed_at 컬럼을 업데이트한다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InternalKafkaListener {

    private final MemberOutboxUseCase memberOutboxUseCase;
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
    public void listenInternalMemberCreate(
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) throws JsonProcessingException {
        // 1. record 값을 이벤트 객체로 변환한다.
        String jsonValue = record.value();
        OutboxEvent event = objectMapper.readValue(jsonValue, MemberCreateEvent.class);

        // 2. outbox 테이블에 이벤트 처리 상태를 업데이트한다.
        memberOutboxUseCase.markOutboxEventProcessed(event);

        // 3. ack 처리
        acknowledgment.acknowledge();
    }


    /**
     * @apiNote Kafka 내부 리스너
     * Outbox 테이블에 message_status와 processed_at 컬럼을 업데이트한다.
     */
    @TraceOutboxKafka
    @KafkaListener(
            topics = {"member-nickname-change-outbox"},
            groupId = "member-group-nickname-change",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenInternalNicknameChange(
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) throws JsonProcessingException {
        // 1. record 값을 이벤트 객체로 변환한다.
        String jsonValue = record.value();
        OutboxEvent event = objectMapper.readValue(jsonValue, NicknameChangeEvent.class);

        // 2. outbox 테이블에 이벤트 처리 상태를 업데이트한다.
        memberOutboxUseCase.markOutboxEventProcessed(event);

        // 3. ack 처리
        acknowledgment.acknowledge();
    }


    /**
     * @apiNote Kafka 내부 리스너
     * Outbox 테이블에 message_status와 processed_at 컬럼을 업데이트한다.
     */
    @TraceOutboxKafka
    @KafkaListener(
            topics = {"member-profile-image-change-outbox"},
            groupId = "member-group-profile-image-change",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenInternalProfileImageChange(
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) throws JsonProcessingException {
        // 1. record 값을 이벤트 객체로 변환한다.
        String jsonValue = record.value();
        OutboxEvent event = objectMapper.readValue(jsonValue, ProfileImageChangeEvent.class);

        // 2. outbox 테이블에 이벤트 처리 상태를 업데이트한다.
        memberOutboxUseCase.markOutboxEventProcessed(event);

        // 3. ack 처리
        acknowledgment.acknowledge();
    }

}
