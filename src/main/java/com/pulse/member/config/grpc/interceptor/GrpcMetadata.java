package com.pulse.member.config.grpc.interceptor;

import io.grpc.Context;
import io.grpc.Metadata;

/**
 * gRPC 헤더 추출을 위한 메타데이터 상수
 */
public class GrpcMetadata {

    public static final Metadata.Key<String> TRACEPARENT_KEY = Metadata.Key.of("traceparent", Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<io.opentelemetry.context.Context> OTEL_CONTEXT_KEY = Context.key("otelContext");

}
