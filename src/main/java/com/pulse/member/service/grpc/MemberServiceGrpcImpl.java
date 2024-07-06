package com.pulse.member.service.grpc;

import com.pulse.event_library.service.OutboxService;
import com.pulse.member.config.trace.grpc.GrpcMetadata;
import com.pulse.member.config.trace.grpc.GrpcMetadataInterceptor;
import com.pulse.member.dto.MemberCreateDTO;
import com.pulse.member.dto.MemberRetrieveDTO;
import com.pulse.member.listener.spring.event.MemberCreateEvent;
import com.pulse.member.grpc.MemberProto;
import com.pulse.member.grpc.MemberServiceGrpc;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.service.usecase.MemberService;
import io.grpc.Metadata;
import io.grpc.stub.StreamObserver;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * MemberServiceImpl 클래스는 gRPC 서버를 구현한 것으로, gRPC 서버 메서드를 구현합니다.
 * 이 클래스가 RestController와 같은 역할을 합니다.
 * api테스트는 grpcurl을 사용하여 gRPC 서버 메서드를 호출할 수 있습니다.
 *
 * @apiNote gRPC 서버 메서드를 구현하려면, *.proto 파일을 먼저 컴파일해야 합니다.
 * 예를 들면 member.proto를 먼저 컴파일하고, 생성된 MemberServiceGrpc.MemberServiceImplBase를 상속받아 구현합니다.
 */
@Slf4j
@RequiredArgsConstructor
@GrpcService
public class MemberServiceGrpcImpl extends MemberServiceGrpc.MemberServiceImplBase {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final OutboxService outboxService;
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("grpc-server");

    /**
     * id로 회원 조회
     * 요청 예시:
     * grpcurl -plaintext -d '{"id": 1}' localhost:50051 MemberService/GetMemberById
     *
     * @param request          - 요청
     * @param responseObserver - 응답
     */
    @Override
    public void getMemberById(
            MemberProto.MemberIdRequest request,
            StreamObserver<MemberProto.MemberRetrieveResponse> responseObserver
    ) {
        // 1. gRPC Context에서 OpenTelemetry Context를 추출합니다. (인터셉터에서 traceId를 추출하여 세팅한 컨텍스트를 사용합니다.)
        io.opentelemetry.context.Context otelContext = GrpcMetadata.OTEL_CONTEXT_KEY.get();

        // 2. Span을 생성합니다. (생성한 otelContext를 부모로 설정합니다. 이렇게 하면 부모 Span의 traceId를 사용합니다.)
        Span span = tracer.spanBuilder("grpc-server")
                .setParent(otelContext)
                .startSpan();

        // 3. 이벤트를 발행하고 Span을 현재 컨텍스트에 설정합니다.
        MemberCreateEvent event = new MemberCreateEvent(request.getId());
        try (Scope scope = span.makeCurrent()) {
            MemberRetrieveDTO member = memberService.getMemberById(request.getId());
            MemberProto.MemberRetrieveResponse response = memberMapper.toProto(member);

            // 4. 응답을 클라이언트에게 보냅니다.
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            // 5. outbox 테이블에 이벤트 기록을 성공으로 처리
            outboxService.markOutboxEventSuccess(event);
        } catch (Exception e) {
            // 6. 에러 발생 시 에러를 클라이언트에게 전달하고 스팬에 기록합니다.
            span.recordException(e);
            responseObserver.onError(e);
            outboxService.markOutboxEventFailed(event);
        } finally {
            // 7. Span 종료
            span.end();
        }
    }

    /**
     * 회원 생성 + 이벤트 발행
     * 요청 예시:
     * grpcurl -plaintext -d '{
     * "email": "test252@example.com",
     * "password": "1234",
     * "name": "Test User",
     * }' localhost:50051 MemberService/CreateMember
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void createMember(
            MemberProto.MemberRequest request,
            StreamObserver<MemberProto.MemberCreateResponse> responseObserver
    ) {
        Span span = tracer.spanBuilder("create-member").startSpan();
        try (Scope scope = span.makeCurrent()) {

            MemberCreateDTO memberCreateDTO = memberMapper.toCreateDto(request);
            MemberCreateDTO createdMember = memberService.createMember(memberCreateDTO);
            MemberProto.MemberCreateResponse response = memberMapper.toProto(createdMember);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            span.recordException(e);
            responseObserver.onError(e);
        } finally {
            span.end();
        }
    }

}
