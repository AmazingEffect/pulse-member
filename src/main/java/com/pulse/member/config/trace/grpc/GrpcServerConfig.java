package com.pulse.member.config.trace.grpc;

import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * gRPC 서버 설정 - 인터셉터를 설정하는 클래스
 */
@Configuration
public class GrpcServerConfig {

    @Bean
    @GrpcGlobalServerInterceptor
    public ServerInterceptor customMetadataInterceptor() {
        return new GrpcMetadataInterceptor();
    }

}