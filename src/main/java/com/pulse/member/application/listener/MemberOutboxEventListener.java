package com.pulse.member.application.listener;

import com.pulse.member.adapter.out.event.outbox.OutboxEvent;
import com.pulse.member.application.port.in.kafka.KafkaProducerUseCase;
import com.pulse.member.application.port.in.outbox.MemberOutboxUseCase;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Outbox 테이블과 관련된 스프링 이벤트를 처리하는 리스너
 */
@RequiredArgsConstructor
@Component
public class MemberOutboxEventListener {

    private final MemberOutboxUseCase memberOutboxUseCase;
    private final KafkaProducerUseCase kafkaProducerUseCase;
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("outbox-event-listener");


    /**
     * @param event Outbox 이벤트
     * @apiNote 이벤트가 발행되면 트랜잭션이 커밋되기 전에 Outbox 테이블에 저장하고 커밋한다.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOutboxEvent(OutboxEvent event) {
        // 1. Span을 생성하고 현재 컨텍스트에 설정
        Span span = tracer.spanBuilder("[spring-event] before-commit (outbox-table save)").startSpan();

        // 2. Span을 현재 컨텍스트에 설정
        try (Scope scope = span.makeCurrent()) {
            Context context = Context.current().with(span);
            try {
                // Outbox 테이블에 이벤트를 저장한다.
                memberOutboxUseCase.saveOutboxEvent(event);
            } catch (Exception e) {
                span.recordException(e);
                throw e;
            } finally {
                span.end();
            }
        }
    }


    /**
     * @param event 전송할 Outbox 이벤트
     * @apiNote 트랜잭션이 성공적으로 커밋되면 Kafka 메시지를 전송합니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendToKafka(OutboxEvent event) {
        // 1. 현재 컨텍스트를 가져와 Span을 생성
        Span span = tracer.spanBuilder("[spring-event] after-commit (kafka-produce && outbox table process)").startSpan();

        // 2. Span을 현재 컨텍스트에 설정
        try (Scope scope = span.makeCurrent()) {
            Context context = Context.current().with(span);
            try {
                // 2-1. 메시지로 보낼 payload와 전송할 Kafka의 토픽 정보를 가져옵니다.
                Long message = event.getPayload();
                String topic = memberOutboxUseCase.getKafkaTopic(event);

                // 2-2. 추출한 토픽에 Kafka 메시지를 전송합니다.
                kafkaProducerUseCase.sendWithRetry(topic, String.valueOf(message), context);

                // 2-3. Outbox 이벤트를 처리 대기 상태로 변경합니다.
                memberOutboxUseCase.markOutboxEventPending(event);
            } catch (Exception e) {
                // exception: 예외 발생 시 Outbox 이벤트를 실패 상태로 변경하고 Span에 예외를 기록합니다.
                memberOutboxUseCase.markOutboxEventFailed(event);
                span.recordException(e);
            }
        } finally {
            // Span을 종료합니다.
            span.end();
        }
    }

}
