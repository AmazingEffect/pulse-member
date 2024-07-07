package com.pulse.member.config.trace.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ZeroPayload로 gRPC에 요청을 보내는 Kafka Consumer의 메서드에 적용해서 Span을 적용하는 어노테이션
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceZeroPayloadKafka {
}
