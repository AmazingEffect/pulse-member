package com.pulse.member.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse.member.adapter.out.event.outbox.OutboxEvent;
import com.pulse.member.application.port.in.outbox.MemberOutboxUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Outbox 테이블의 상태를 업데이트하는 리스너
 */
@Deprecated
@RequiredArgsConstructor
@Component
public class OutboxStatusChangeKafkaListener {

    private final MemberOutboxUseCase memberOutboxUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

//
//    /**
//     * @param record         ConsumerRecord 객체
//     * @param acknowledgment ack 객체
//     * @apiNote outbox로 끝나는 토픽만 처리하는 listen 메서드
//     * topicPattern을 사용하여 outbox로 끝나는 토픽만 처리한다.
//     */
//    @KafkaListener(topicPattern = ".*outbox$")
//    public void outboxStatusChangeListener(
//            ConsumerRecord<String, String> record,
//            Acknowledgment acknowledgment,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
//            @Header(KafkaHeaders.OFFSET) long offset
//    ) throws JsonProcessingException {
//        // 1. record 값을 이벤트 객체로 변환한다.
//        String jsonValue = record.value();
//        OutboxEvent event = objectMapper.readValue(jsonValue, OutboxEvent.class);
//
//        // 2. kafka로부터 메시지를 수신한다면 내부 Outbox 테이블의 상태를 PROCESSING으로 변경한다.
//        memberOutboxUseCase.markOutboxEventProcessed(event);
//
//        // 3. ack 처리
//        acknowledgment.acknowledge();
//    }

}
