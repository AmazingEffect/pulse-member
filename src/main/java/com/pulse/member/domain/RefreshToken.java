package com.pulse.member.domain;

import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 리프레시 토큰
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

    private Long id;
    private Member member;
    private String token;
    private LocalDateTime expiryDate;

    // factory method
    public static RefreshToken of(String requestRefreshToken) {
        return RefreshToken.builder()
                .token(requestRefreshToken)
                .build();
    }

    // factory method
    public static RefreshToken of(Member member, long refreshTokenDurationMinutes) {
        return RefreshToken.builder()
                .member(member)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusMinutes(refreshTokenDurationMinutes))
                .build();
    }


    /**
     * 토큰의 만료 여부 확인
     */
    public void verifyTokenExpiration() {
        verifyTokenInnerMember();
        if (this.expiryDate.isBefore(LocalDateTime.now())) {
            throw new MemberException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }


    /**
     * 토큰 내부 회원 정보 확인
     */
    private void verifyTokenInnerMember() {
        if (this.member == null) {
            throw new MemberException(ErrorCode.REFRESH_TOKEN_NOT_HAVE_MEMBER);
        }
    }

}
