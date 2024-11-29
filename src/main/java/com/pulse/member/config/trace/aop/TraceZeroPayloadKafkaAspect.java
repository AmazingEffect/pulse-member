package com.pulse.member.config.trace.aop;

import com.pulse.member.util.TraceUtil;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Kafka 리스너 동작 시 Span을 생성하고 종료하는 Aspect
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class TraceZeroPayloadKafkaAspect {

    private final Tracer tracer = GlobalOpenTelemetry.getTracer("kafka-consumer");
    private final TraceUtil traceUtil;

    /**
     * Kafka 리스너 호출 시 Span을 생성하고 종료한다.
     *
     * @param joinPoint - 프록시 대상 메서드
     * @param record    - Kafka 메시지
     * @throws Throwable - 예외
     */
    @Around(value = "@annotation(com.pulse.member.config.trace.annotation.TraceZeroPayloadKafka) && " +
            "args(record, acknowledgment, partition, offset)",
            argNames = "joinPoint,record,acknowledgment,partition,offset"
    )
    public Object traceKafkaListener(
            ProceedingJoinPoint joinPoint,
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment,
            int partition,
            long offset
    ) throws Throwable {
        // 1. Context를 추출한다.
        Context extractedContext = traceUtil.extractContextFromRecord(record);

        // 2. 기존 Trace를 기반으로 새로운 Span을 생성
        Span span = tracer.spanBuilder("Member [kafka-consume] zeropayload gRPC call")
                .setParent(extractedContext)  // 기존 Trace의 Context를 부모로 설정
                .startSpan();

        // 3. Span을 현재 컨텍스트에 설정
        try (Scope scope = span.makeCurrent()) {
            // 3-1. 실제 메서드 호출
            return joinPoint.proceed();
        } catch (Exception e) {
            // 3-2. 예외 발생 시 Span에 기록
            span.recordException(e);
            throw e;
        } finally {
            // 4. Span 종료
            log.info("Ending traceId: {}, spanId: {}", span.getSpanContext().getTraceId(), span.getSpanContext().getSpanId());
            span.end();
        }
    }
}
