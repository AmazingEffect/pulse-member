package com.pulse.member.config.grpc.interceptor;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * gRPC 서버 인터셉터 클래스. 이 클래스는 gRPC 요청에서 JWT 토큰을 추출하고 인증을 수행합니다.
 */
@RequiredArgsConstructor
@Component
public class JwtGrpcInterceptor implements ServerInterceptor {

    private final CustomGrpcAuthenticationReader customGrpcAuthenticationReader;

    /**
     * gRPC 호출을 인터셉트하여 JWT 토큰을 검증하고 인증 정보를 설정합니다.
     *
     * @param call    gRPC 서버 콜 객체
     * @param headers gRPC 메타데이터(헤더) 객체
     * @param next    다음 서버 콜 핸들러
     * @param <ReqT>  요청 타입
     * @param <RespT> 응답 타입
     * @return gRPC 서버 콜 리스너
     */
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        // CustomGrpcAuthenticationReader를 사용하여 인증 정보를 읽어옵니다.
        Authentication authentication = customGrpcAuthenticationReader.readAuthentication(call, headers);
        if (authentication != null) {
            // 인증 정보를 SecurityContext에 설정합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return next.startCall(call, headers);
        }

        // 인증 실패 시 호출을 종료합니다.
        call.close(Status.UNAUTHENTICATED.withDescription("Invalid JWT token"), headers);
        return new ServerCall.Listener<ReqT>() {
        };
    }

}
