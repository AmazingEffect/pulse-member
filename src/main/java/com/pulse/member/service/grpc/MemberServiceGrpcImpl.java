package com.pulse.member.service.grpc;

import com.pulse.event_library.service.OutboxService;
import com.pulse.member.dto.MemberDTO;
import com.pulse.member.event.spring.MemberCreateEvent;
import com.pulse.member.grpc.MemberProto;
import com.pulse.member.grpc.MemberServiceGrpc;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.service.usecase.MemberService;
import io.grpc.stub.StreamObserver;
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
        MemberCreateEvent event = new MemberCreateEvent(request.getId());

        try {
            // 1. MemberService를 사용하여 ID로 회원 조회 (조회라서 outboxService와 트랜잭션을 하나로 묶지 않음)
            MemberDTO member = memberService.getMemberById(request.getId());
            // 2. 조회한 회원 정보를 MemberProto.MemberResponse로 변환
            MemberProto.MemberResponse response = memberMapper.toProto(member);

            // 3. 응답을 클라이언트에게 보냅니다. (비동기가 아님)
            responseObserver.onNext(response);
            responseObserver.onCompleted();

            // 4. outbox 테이블에 이벤트 기록을 성공으로 처리
            outboxService.markOutboxEventSuccess(event);
        } catch (Exception e) {
            // 5. 에러 발생 시 에러를 클라이언트에게 전달
            responseObserver.onError(e);

            // 6. outbox 테이블에 이벤트 기록을 실패로 처리
            outboxService.markOutboxEventFailed(event);
        }
    }

    /**
     * 회원 생성 + 이벤트 발행
     * 요청 예시:
     * grpcurl -plaintext -d '{
     *   "email": "test252@example.com",
     *   "password": "1234",
     *   "name": "Test User",
     *   "profilePictureUrl": "http://example.com/profile.jpg",
     *   "introduction": "Hello, I am a test user.",
     *   "phoneNumber": "123-456-7890",
     *   "address": "123 Test Street, Test City, TX 12345",
     *   "birthDate": "1990-01-01T00:00:00",
     *   "gender": "Male",
     *   "website": "http://example.com",
     *   "statusMessage": "Feeling good!",
     *   "accountStatus": "Active",
     *   "joinedDate": "2024-06-29T13:00:00",
     *   "lastLogin": "2024-06-29T13:30:00"
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
        MemberDTO memberDTO = memberMapper.toDto(request);
        MemberDTO createdMember = memberService.createMember(memberDTO);
        MemberProto.MemberResponse response = memberMapper.toProto(createdMember);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
