package com.pulse.member.config.grpc;

import com.pulse.member.config.grpc.interceptor.ExceptionHandlingInterceptor;
import com.pulse.member.config.grpc.interceptor.GrpcMetadataInterceptor;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Configuration;

/**
 * gRPC 서버 설정 - 인터셉터를 설정하는 클래스
 * gRPC 인터셉터는 Spring Boot에서 @GrpcGlobalServerInterceptor 어노테이션을 사용하여 전역적으로 등록할 수 있습니다.
 * 한 번 설정해 두면, 해당 인터셉터는 모든 gRPC 요청에 대해 자동으로 적용됩니다.
 * GrpcConfig 클래스에서 인터셉터를 전역적으로 등록했기 때문에, gRPC 서버 클래스 내의 모든 gRPC 메서드는 자동으로 이 인터셉터의 영향을 받습니다.
 * 예를 들어, ExceptionHandlingInterceptor가 전역 인터셉터로 설정되어 있다면, 모든 gRPC 호출에서 발생하는 예외는 이 인터셉터를 통해 처리됩니다.
 */
@RequiredArgsConstructor
@Configuration
public class GrpcConfig {

//    private final CustomGrpcAuthenticationReader customGrpcAuthenticationReader;

    /**
     * gRPC 서버 메서드 호출 시, 요청 헤더에서 traceparent 헤더를 추출하여 SpanContext를 생성합니다.
     */
    @GrpcGlobalServerInterceptor
    GrpcMetadataInterceptor grpcMetadataInterceptor() {
        return new GrpcMetadataInterceptor();
    }


    /**
     * gRPC 예외 처리 인터셉터 설정
     */
    @GrpcGlobalServerInterceptor
    ExceptionHandlingInterceptor exceptionHandlingInterceptor() {
        return new ExceptionHandlingInterceptor();
    }


    /**
     * gRPC 서버 인터셉터 클래스. 이 클래스는 gRPC 요청에서 JWT 토큰을 추출하고 인증을 수행합니다.
     */
//    @Bean
//    @GrpcGlobalServerInterceptor
//    JwtGrpcInterceptor jwtGrpcInterceptor() {
//        return new JwtGrpcInterceptor(customGrpcAuthenticationReader);
//    }

}