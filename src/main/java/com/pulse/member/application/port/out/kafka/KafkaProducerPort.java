package com.pulse.member.application.port.out.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface KafkaProducerPort {

    CompletableFuture<SendResult<String, String>> sendKafkaMessage(ProducerRecord<String, String> record);

}
