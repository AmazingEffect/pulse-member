package com.pulse.member.adapter.out.kafka;

import com.pulse.member.application.port.out.kafka.KafkaProducerPort;
import com.pulse.member.common.annotation.MessagingAdapter;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapSetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * KafkaProducerAdapter는 Kafka 메시지 전송을 처리하는 어댑터입니다.
 * 이 어댑터는 비즈니스 로직, 트레이싱, 재시도 등의 모든 로직을 포함하고 있습니다.
 */
@RequiredArgsConstructor
@Slf4j
@MessagingAdapter
public class KafkaProducerAdapter implements KafkaProducerPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("kafka-producer");

    // Kafka 메시지에 트레이스 컨텍스트를 주입하기 위한 TextMapSetter
    private static final TextMapSetter<ProducerRecord<String, String>> setter =
            (carrier, key, value) -> carrier.headers().add(key, value.getBytes(StandardCharsets.UTF_8));

    /**
     * Kafka로 메시지를 전송합니다.
     *
     * @param topic       - 전송할 Kafka 토픽
     * @param payloadJson - 전송할 메시지
     * @param context     - 전송에 사용될 컨텍스트
     * @return 전송 결과를 나타내는 CompletableFuture
     */
    @Override
    public CompletableFuture<SendResult<String, String>> sendMessageWithoutKey(
            String topic,
            String payloadJson,
            Context context
    ) {
        // 메시지 키 없이 Kafka로 전송
        return sendMessageWithKey(topic, null, payloadJson, context);
    }

    /**
     * Kafka로 메시지를 전송합니다. 메시지 키가 포함된 경우입니다.
     *
     * @param topic       - 전송할 Kafka 토픽
     * @param key         - Kafka 메시지 키
     * @param payloadJson - 전송할 메시지
     * @param context     - 전송에 사용될 컨텍스트
     * @return 전송 결과를 나타내는 CompletableFuture
     */
    @Override
    public CompletableFuture<SendResult<String, String>> sendMessageWithKey(
            String topic,
            String key,
            String payloadJson,
            Context context
    ) {
        // OpenTelemetry Span 생성 및 설정
        Span span = tracer.spanBuilder("[kafka] : message-produce").setParent(context).startSpan();

        try (Scope scope = span.makeCurrent()) {
            // Kafka 메시지 레코드를 생성합니다.
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payloadJson);

            // Traceparent 헤더를 Kafka 레코드에 주입하여 트레이스를 연결합니다. (리스너에서 가져다 사용)
            GlobalOpenTelemetry.getPropagators().getTextMapPropagator().inject(context, record, setter);

            // Kafka 메시지 전송
            return kafkaTemplate.send(record);
        } finally {
            // Span 종료
            span.end();
        }
    }

    /**
     * Kafka 메시지를 재시도 로직을 포함하여 전송합니다 (메시지 키 없이).
     *
     * @param topic       - 전송할 Kafka 토픽
     * @param payloadJson - 전송할 메시지
     * @param context     - 전송에 사용될 컨텍스트
     */
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    @Override
    public void sendWithRetryWithoutKey(
            String topic,
            String payloadJson,
            Context context
    ) {
        sendMessageWithoutKey(topic, payloadJson, context).whenComplete((result, ex) -> {
            handleSendResult(topic, payloadJson, ex, result);
        });
    }

    /**
     * Kafka 메시지를 재시도 로직을 포함하여 전송합니다 (메시지 키 포함).
     *
     * @param topic       - 전송할 Kafka 토픽
     * @param key         - Kafka 메시지 키
     * @param payloadJson - 전송할 메시지
     * @param context     - 전송에 사용될 컨텍스트
     */
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    @Override
    public void sendWithRetryWithKey(
            String topic,
            String key,
            String payloadJson,
            Context context
    ) {
        sendMessageWithKey(topic, key, payloadJson, context).whenComplete((result, ex) -> {
            handleSendResult(topic, payloadJson, ex, result);
        });
    }

    /**
     * 메시지 전송 결과를 처리합니다.
     *
     * @param topic       - 전송된 Kafka 토픽
     * @param payloadJson - 전송된 메시지
     * @param ex          - 전송 중 발생한 예외
     * @param result      - 전송 결과
     */
    private void handleSendResult(
            String topic,
            String payloadJson,
            Throwable ex,
            SendResult<String, String> result
    ) {
        if (ex != null) {
            log.error("Failed to send message to Kafka topic {}: {}", topic, ex.getMessage());
            // 알림 시스템을 통한 예외 처리 (slack, email, etc...)
            throw new RuntimeException("Failed to send message to Kafka", ex);
        }

        // 성공 시 전송된 메시지의 오프셋을 로그에 기록합니다.
        RecordMetadata metadata = result.getRecordMetadata();
        log.info("Sent message=[{}] to topic=[{}] with offset=[{}]", payloadJson, topic, metadata.offset());
    }

}
