package com.pulse.member.config.security.grpc;

import com.pulse.member.config.jwt.JwtTokenProvider;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * gRPC 메타데이터에서 JWT 토큰을 읽어 Spring Security 인증 객체로 변환하는 데 중요한 역할을 합니다. 이 구성 요소는 gRPC 요청을 인증할 수 있도록 도와줍니다.
 */
@RequiredArgsConstructor
@Component
public class CustomGrpcAuthenticationReader implements GrpcAuthenticationReader {

    private final JwtTokenProvider jwtTokenProvider;


    /**
     * gRPC 메타데이터에서 JWT 토큰을 추출하고 인증 객체를 반환합니다.
     *
     * @param call    gRPC 서버 콜 객체
     * @param headers gRPC 메타데이터(헤더) 객체
     * @return 인증 객체
     * @throws AuthenticationException 인증 예외
     */
    @Nullable
    @Override
    public Authentication readAuthentication(ServerCall<?, ?> call, Metadata headers) throws AuthenticationException {
        // gRPC 메타데이터에서 'Authorization' 헤더를 추출합니다.
        String authorizationHeader = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));

        // 헤더가 존재하고 'Bearer '로 시작하는지 확인합니다.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 헤더에서 토큰 부분을 추출합니다.
            String token = authorizationHeader.substring(7);
            // JwtTokenProvider를 사용하여 토큰에서 Authentication 객체를 가져옵니다.
            return jwtTokenProvider.getAuthenticationFromGrpcToken(token);
        }
        // 헤더가 없거나 유효하지 않으면 null을 반환합니다.
        return null;
    }

}
