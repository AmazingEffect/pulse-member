package com.pulse.member.adapter.in.grpc;

import com.pulse.member.adapter.out.event.MemberCreateEvent;
import com.pulse.member.application.port.in.member.FindMemberUseCase;
import com.pulse.member.application.port.in.outbox.MemberOutboxUseCase;
import com.pulse.member.config.trace.annotation.TraceGrpcServer;
import com.pulse.member.domain.Member;
import com.pulse.member.grpc.MemberProto;
import com.pulse.member.grpc.MemberServiceGrpc;
import com.pulse.member.mapper.MemberMapper;
import io.grpc.stub.StreamObserver;
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

    private final FindMemberUseCase memberUseCase;
    private final MemberMapper memberMapper;
    private final MemberOutboxUseCase memberOutboxUseCase;


    /**
     * @param request          - 요청
     * @param responseObserver - 응답
     * @apiNote id로 회원 조회
     * 요청 예시:
     * grpcurl -plaintext -d '{"id": 1}' localhost:50051 MemberService/GetMemberById
     */
    @TraceGrpcServer
    @Override
    public void getMemberById(
            MemberProto.MemberIdRequest request,
            StreamObserver<MemberProto.MemberRetrieveResponse> responseObserver
    ) {
        MemberCreateEvent event = new MemberCreateEvent(request.getId());
        try {
            Member member = Member.of(request.getId());
            Member findMember = memberUseCase.findMemberById(member);
            MemberProto.MemberRetrieveResponse response = memberMapper.toProto(findMember);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

            // outbox 이벤트 성공 처리
            memberOutboxUseCase.markOutboxEventSuccess(event);
        } catch (Exception e) {
            // 예외 발생 시 outbox 이벤트 실패 처리
            memberOutboxUseCase.markOutboxEventFailed(event);
            responseObserver.onError(e);
            throw e; // 예외를 다시 던져 AOP에서 처리되도록 함
        }
    }

}
