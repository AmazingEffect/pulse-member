package com.pulse.member.config.grpc.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * ExceptionHandlingInterceptor는 gRPC 서버 인터셉터로,
 * gRPC 메서드 호출 중 발생하는 예외를 중앙에서 처리합니다.
 * 예외 발생 시 적절한 gRPC 상태 코드와 메시지를 반환합니다.
 */
@Slf4j
public class ExceptionHandlingInterceptor implements ServerInterceptor {

    /**
     * gRPC 메서드 호출을 가로채고, 예외를 처리합니다.
     *
     * @param serverCall       gRPC 서버 호출 객체
     * @param metadata         gRPC 메타데이터
     * @param serverCallHandler gRPC 서버 호출 핸들러
     * @param <ReqT>           요청 타입
     * @param <RespT>          응답 타입
     * @return 요청 리스너
     */
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> serverCall,
            Metadata metadata,
            ServerCallHandler<ReqT, RespT> serverCallHandler
    ) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(
                serverCallHandler.startCall(new ExceptionHandlingServerCall<>(serverCall), metadata)) {

            /**
             * 클라이언트의 요청 처리를 시도합니다.
             * 예외가 발생하면 handleException 메서드가 호출됩니다.
             */
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception e) {
                    handleException(serverCall, e);
                }
            }
        };
    }


    /**
     * 예외를 처리하고 적절한 gRPC 상태 코드와 메시지를 반환합니다.
     *
     * @param call gRPC 서버 호출 객체
     * @param e    발생한 예외
     * @param <RespT> 응답 타입
     */
    private <RespT> void handleException(ServerCall<RespT, ?> call, Exception e) {
        Status status;
        if (e instanceof IllegalArgumentException) {
            // 잘못된 인수 예외 처리
            status = Status.INVALID_ARGUMENT.withDescription(e.getMessage());
        } else {
            // 알 수 없는 예외 처리
            status = Status.UNKNOWN.withDescription("Unknown error occurred").withCause(e);
        }
        // 예외 로그 기록
        log.error("Exception: ", e);
        // gRPC 호출 종료 및 상태 코드 반환
        call.close(status, new Metadata());
    }


    /**
     * ExceptionHandlingServerCall은 ForwardingServerCall을 확장하여,
     * 예외 처리를 위한 추가 기능을 제공합니다.
     *
     * @param <ReqT> 요청 타입
     * @param <RespT> 응답 타입
     */
    private static class ExceptionHandlingServerCall<ReqT, RespT>
            extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> {

        /**
         * ExceptionHandlingServerCall 생성자
         *
         * @param delegate 실제 gRPC 서버 호출 객체
         */
        protected ExceptionHandlingServerCall(ServerCall<ReqT, RespT> delegate) {
            super(delegate);
        }
    }

}
