package com.pulse.member.adapter.out.persistence.adapter.kafka;

import com.pulse.member.application.port.out.kafka.KafkaProducerPort;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * KafkaAdapter는 Kafka로 메시지를 전송하는 어댑터입니다.
 */
@RequiredArgsConstructor
@Component
public class KafkaProducerAdapter implements KafkaProducerPort {

    private final KafkaTemplate<String, String> kafkaTemplate;


    /**
     * @param record 전송할 ProducerRecord
     * @return 전송 결과를 나타내는 CompletableFuture
     * @apiNote KafkaProducerPort를 구현한 메서드로, ProducerRecord를 전달받아 Kafka로 메시지를 전송합니다.
     */
    @Override
    public CompletableFuture<SendResult<String, String>> sendKafkaMessage(
            ProducerRecord<String, String> record
    ) {
        return kafkaTemplate.send(record);
    }

}
