package com.pulse.member.application.port.in.kafka;

import io.opentelemetry.context.Context;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface KafkaProducerUseCase {

    CompletableFuture<SendResult<String, String>> send(String topic, String payloadJson, Context context);

    CompletableFuture<SendResult<String, String>> send(String topic, String key, String payloadJson, Context context);

    void sendWithRetry(String topic, String payloadJson, Context context);

    void sendWithRetry(String topic, String key, String payloadJson, Context context);

}
