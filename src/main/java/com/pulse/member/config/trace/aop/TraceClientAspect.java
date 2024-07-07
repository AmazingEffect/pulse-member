package com.pulse.member.config.trace.aop;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * gRPC 클라이언트 호출 시 Span을 생성하고 종료하는 Aspect
 */
@Aspect
@Component
public class TraceClientAspect {

    private final Tracer tracer = GlobalOpenTelemetry.getTracer("grpc-client");

    /**
     * gRPC 클라이언트 호출 시 Span을 생성하고 종료한다.
     * args(id, context)는 AOP 포인트컷 표현식에서 지정된 메서드가 id와 context라는 매개변수를 가져야만 포인트컷이 적용된다는 것을 의미한다.
     * 즉, 해당 어노테이션이 적용된 메서드 중에서 매개변수의 타입과 순서가 id (Long 타입), context (Context 타입)인 메서드에만 AOP 어드바이스가 적용된다.
     *
     * @param joinPoint - 프록시 대상 메서드
     * @param id        - 회원 id
     * @param context   - OpenTelemetry Context
     * @throws Throwable - 예외
     */
    @Around(value = "@annotation(com.pulse.member.config.trace.annotation.TraceGrpcClient) && args(id, context)", argNames = "joinPoint,id,context")
    public Object traceGrpcClient(ProceedingJoinPoint joinPoint, Long id, Context context) throws Throwable {
        // 1. Span 생성
        Span span = tracer.spanBuilder("Member [grpc] client request")
                .setParent(context)
                .startSpan();

        // 2. Span을 현재 컨텍스트에 설정
        try (Scope scope = span.makeCurrent()) {
            // 2-1. 실제 메서드 호출
            return joinPoint.proceed();
        } catch (Exception e) {
            // 2-2. 예외 발생 시 Span에 기록
            span.recordException(e);
            throw e;
        } finally {
            // 3. Span 종료
            span.end();
        }
    }

}
