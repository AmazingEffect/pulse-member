package com.pulse.member.config.trace.aop;

import com.pulse.member.config.grpc.interceptor.GrpcMetadata;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * gRPC 서버 호출 시 Span을 생성하고 종료하는 Aspect
 */
@Aspect
@Component
@Slf4j
public class TraceGrpcServerAspect {

    private final Tracer tracer = GlobalOpenTelemetry.getTracer("grpc-server");

    /**
     * gRPC 서버 호출 시 Span을 생성하고 종료한다.
     * AOP 포인트컷 표현식에서 지정된 메서드에만 AOP 어드바이스가 적용된다.
     *
     * @param joinPoint - 프록시 대상 메서드
     * @throws Throwable - 예외
     */
    @Around("@annotation(com.pulse.member.config.trace.annotation.TraceGrpcServer)")
    public Object traceGrpcServer(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. gRPC Context에서 OpenTelemetry Context를 추출합니다. (인터셉터에서 traceId를 추출하여 세팅한 컨텍스트를 사용합니다.)
        io.opentelemetry.context.Context otelContext = GrpcMetadata.OTEL_CONTEXT_KEY.get();

        // 2. Span을 생성합니다. (생성한 otelContext를 부모로 설정합니다. 이렇게 하면 부모 Span의 traceId를 사용합니다.)
        Span span = tracer.spanBuilder("Member [grpc] server response")
                .setParent(otelContext)
                .startSpan();

        // Span을 현재 컨텍스트에 설정하고 메서드를 실행합니다.
        try (Scope scope = span.makeCurrent()) {
            return joinPoint.proceed();
        } catch (Throwable e) {
            // 에러 발생 시 Span에 기록합니다.
            span.recordException(e);
            throw e; // 예외를 다시 던집니다.
        } finally {
            // Span을 종료합니다.
            span.end();
        }
    }

}
