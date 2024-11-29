package com.pulse.member.application.port.out.kafka;

import io.opentelemetry.context.Context;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface KafkaProducerPort {

    /**
     * Kafka로 메시지를 전송합니다 (키 없이).
     *
     * @param topic       - 전송할 Kafka 토픽
     * @param payloadJson - 전송할 메시지
     * @param context     - 전송에 사용될 컨텍스트
     * @return 전송 결과를 나타내는 CompletableFuture
     * @apiNote 메시지 키가 없는 Kafka 메시지를 전송하는 경우 사용합니다.
     */
    CompletableFuture<SendResult<String, String>> sendMessageWithoutKey(
            String topic,
            String payloadJson,
            Context context
    );

    /**
     * Kafka로 메시지를 전송합니다 (키 포함).
     *
     * @param topic       - 전송할 Kafka 토픽
     * @param key         - Kafka 메시지 키
     * @param payloadJson - 전송할 메시지
     * @param context     - 전송에 사용될 컨텍스트
     * @return 전송 결과를 나타내는 CompletableFuture
     * @apiNote 메시지 키가 포함된 Kafka 메시지를 전송하는 경우 사용합니다.
     */
    CompletableFuture<SendResult<String, String>> sendMessageWithKey(
            String topic,
            String key,
            String payloadJson,
            Context context
    );

    /**
     * Kafka 메시지를 재시도 로직을 포함하여 전송합니다 (메시지 키 없이).
     *
     * @param topic       - 전송할 Kafka 토픽
     * @param payloadJson - 전송할 메시지
     * @param context     - 전송에 사용될 컨텍스트
     * @apiNote 재시도 로직을 포함하여 키 없이 Kafka로 메시지를 전송하는 경우 사용합니다.
     */
    void sendWithRetryWithoutKey(
            String topic,
            String payloadJson,
            Context context
    );

    /**
     * Kafka 메시지를 재시도 로직을 포함하여 전송합니다 (메시지 키 포함).
     *
     * @param topic       - 전송할 Kafka 토픽
     * @param key         - Kafka 메시지 키
     * @param payloadJson - 전송할 메시지
     * @param context     - 전송에 사용될 컨텍스트
     * @apiNote 재시도 로직을 포함하여 키가 있는 Kafka 메시지를 전송하는 경우 사용합니다.
     */
    void sendWithRetryWithKey(
            String topic,
            String key,
            String payloadJson,
            Context context
    );

}