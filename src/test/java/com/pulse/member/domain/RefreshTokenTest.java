package com.pulse.member.domain;

import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("[Domain] RefreshToken 단위 테스트")
class RefreshTokenTest {

    @DisplayName("[happy] 가지고 있는 RefreshToken의 회원 정보, 만료 날짜가 존재하고 아직 만료되지 않았다면 유효성 검증에 통과한다.")
    @Test
    void isValid() {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .member(Member.builder().build())
                .token("token")
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();

        // when
        refreshToken.validRefreshToken();

        // then
        Assertions.assertThat(refreshToken).isNotNull();
        Assertions.assertThat(refreshToken.getMember()).isNotNull();
        Assertions.assertThat(refreshToken.getToken()).isEqualTo("token");
        Assertions.assertThat(refreshToken.getExpiryDate()).isAfter(LocalDateTime.now());
    }


    @DisplayName("[bad] RefreshToken의 회원 정보가 없다면 유효성 검증에 실패한다.")
    @Test
    void isInvalidMember() {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .token("token")
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();

        // when & then
        Assertions.assertThatThrownBy(refreshToken::validRefreshToken)
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.REFRESH_TOKEN_NOT_HAVE_MEMBER.getMessage());
    }


    @DisplayName("[bad] RefreshToken의 만료 날짜가 없다면 유효성 검증에 실패한다.")
    @Test
    void isInvalidExpiryDate() {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .member(Member.builder().build())
                .token("token")
                .build();

        // when & then
        Assertions.assertThatThrownBy(refreshToken::validRefreshToken)
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.REFRESH_TOKEN_EXPIRATION_DATE_NOT_FOUND.getMessage());
    }


    @DisplayName("[bad] RefreshToken의 만료 날짜가 현재 시간보다 이전이라면 유효성 검증에 실패한다.")
    @Test
    void isInvalidExpiryDateExpiration() {
        // given
        RefreshToken refreshToken = RefreshToken.builder()
                .member(Member.builder().build())
                .token("token")
                .expiryDate(LocalDateTime.now().minusMinutes(10))
                .build();

        // when & then
        Assertions.assertThatThrownBy(refreshToken::validRefreshToken)
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
    }

}