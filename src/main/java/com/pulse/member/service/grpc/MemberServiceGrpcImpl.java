package com.pulse.member.service.grpc;

import com.pulse.event_library.service.OutboxService;
import com.pulse.member.dto.MemberDTO;
import com.pulse.member.event.spring.MemberCreateEvent;
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
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * MemberServiceImpl 클래스는 gRPC 서버를 구현한 것으로, gRPC 서버 메서드를 구현합니다.
 * 이 클래스가 RestController와 같은 역할을 합니다.
 * api테스트는 grpcurl을 사용하여 gRPC 서버 메서드를 호출할 수 있습니다.
 *
 * @apiNote gRPC 서버 메서드를 구현하려면, *.proto 파일을 먼저 컴파일해야 합니다.
 * 예를 들면 member.proto를 먼저 컴파일하고, 생성된 MemberServiceGrpc.MemberServiceImplBase를 상속받아 구현합니다.
 */
@RequiredArgsConstructor
@GrpcService
public class MemberServiceGrpcImpl extends MemberServiceGrpc.MemberServiceImplBase {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final OutboxService outboxService;
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("grpc-server");

    // gRPC 요청을 위한 TextMapGetter
    private static final TextMapGetter<Metadata> getter = new TextMapGetter<Metadata>() {
        @Override
        public Iterable<String> keys(Metadata carrier) {
            return carrier.keys();
        }

        @Override
        public String get(Metadata carrier, String key) {
            Metadata.Key<String> headerKey = Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER);
            return carrier.get(headerKey);
        }
    };

    /**
     * id로 회원 조회
     * 요청 예시:
     * grpcurl -plaintext -d '{"id": 1}' localhost:50051 MemberService/GetMemberById
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void getMemberById(
            MemberProto.MemberIdRequest request,
            StreamObserver<MemberProto.MemberResponse> responseObserver
    ) {
        // 1. gRPC 메타데이터를 사용하여 컨텍스트를 추출합니다.
        Metadata metadata = new Metadata();
        Context context = GlobalOpenTelemetry.getPropagators().getTextMapPropagator().extract(Context.current(), metadata, getter);

        // 2. Span을 생성하고 부모 컨텍스트를 설정합니다.
        Span span = tracer.spanBuilder("grpc-server")
                .setParent(context)
                .startSpan();

        MemberCreateEvent event = new MemberCreateEvent(request.getId());
        try (Scope scope = span.makeCurrent()) {
            // 3. MemberService를 사용하여 ID로 회원 조회
            MemberDTO member = memberService.getMemberById(request.getId());
            // 4. 조회한 회원 정보를 MemberProto.MemberResponse로 변환
            MemberProto.MemberResponse response = memberMapper.toProto(member);

            // 5. 응답을 클라이언트에게 보냅니다.
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            // 6. outbox 테이블에 이벤트 기록을 성공으로 처리
            outboxService.markOutboxEventSuccess(event);
        } catch (Exception e) {
            // 7. 에러 발생 시 에러를 클라이언트에게 전달하고 스팬에 기록합니다.
            span.recordException(e);
            responseObserver.onError(e);
            outboxService.markOutboxEventFailed(event);
        } finally {
            // 8. Span 종료
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
            StreamObserver<MemberProto.MemberResponse> responseObserver
    ) {
        Span span = tracer.spanBuilder("create-member").startSpan();
        try (Scope scope = span.makeCurrent()) {
            MemberDTO memberDTO = memberMapper.toDto(request);
            MemberDTO createdMember = memberService.createMember(memberDTO);
            MemberProto.MemberResponse response = memberMapper.toProto(createdMember);
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
