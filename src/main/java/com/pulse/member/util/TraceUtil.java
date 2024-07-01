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

@Slf4j
@Component
public class TraceUtil {

    /**
     * Kafka 메시지에서 traceparent 헤더를 추출하여 SpanContext를 생성합니다.
     *
     * @param record
     * @return
     */
    public Context extractContextFromRecord(ConsumerRecord<String, String> record) {
        String traceParent = null;

        for (Header header : record.headers()) {
            String key = header.key();
            String value = new String(header.value(), StandardCharsets.UTF_8);
            log.info("Kafka Header key: {}, value: {}", key, value);
            if ("traceparent".equals(key)) {
                traceParent = value;
            }
        }

        if (traceParent != null) {
            log.info("Extracted traceparent: {}", traceParent);

            String[] parts = traceParent.split("-");
            String traceId = parts[1];
            String spanId = parts[2];
            String traceFlags = parts[3];

            SpanContext spanContext = SpanContext.createFromRemoteParent(
                    traceId,
                    spanId,
                    TraceFlags.fromHex(traceFlags, 0),
                    TraceState.getDefault()
            );

            return Context.current().with(Span.wrap(spanContext));
        } else {
            log.warn("No traceparent header found in the Kafka message");
            return Context.current();
        }
    }

}
