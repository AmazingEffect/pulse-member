package com.pulse.member.util;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * MSA의 분산 추적을 위해 Trace Context를 추출하는 유틸리티 클래스
 * Jaeger를 사용하여 분산 추적을 수행하고 있으며, Kafka 메시지에서 traceparent 헤더를 추출하여 SpanContext를 생성한다.
 */
@Slf4j
@Component
public class TraceUtil {

    /**
     * @param record Kafka 메시지
     * @return 추출한 SpanContext를 포함한 Context
     * @apiNote Kafka 메시지에서 traceparent 헤더를 추출하여 기존의 Context와 연결한다.
     */
    public Context extractContextFromRecord(ConsumerRecord<String, String> record) {
        String traceParent = null;

        // kafka 헤더에서 traceparent를 추출한다.
        for (Header header : record.headers()) {
            String key = header.key();
            String value = new String(header.value(), StandardCharsets.UTF_8);
            log.info("Kafka Header key: {}, value: {}", key, value);

            // key 값이 traceparent인 헤더를 찾아 value를 추출
            if (Constant.TRACE_PARENT.equals(key)) {
                traceParent = value;
            }
        }

        // 만약 traceparent 헤더가 존재한다면 SpanContext를 생성하여 Context에 추가
        if (traceParent != null) {
            log.info("Extracted traceparent: {}", traceParent);

            // Extract traceId, spanId, traceFlags를 traceparent에서 추출
            String[] parts = traceParent.split("-");
            String traceId = parts[1];
            String spanId = parts[2];
            String traceFlags = parts[3];

            // SpanContext 생성
            SpanContext spanContext = SpanContext.createFromRemoteParent(
                    traceId,
                    spanId,
                    TraceFlags.fromHex(traceFlags, 0),
                    TraceState.getDefault()
            );

            // SpanContext를 포함한 Context 반환
            return Context.current().with(Span.wrap(spanContext));
        } else {
            log.warn("No traceparent header found in the Kafka message");
            return Context.current();
        }
    }

}
