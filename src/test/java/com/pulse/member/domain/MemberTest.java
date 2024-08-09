package com.pulse.member.domain;


import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Domain] Member 단위 테스트")
public class MemberTest {

    @DisplayName("[happy] 회원 이메일 변경을 시도하면 이메일이 변경된다.")
    @Test
    public void changeEmail() {
        // given
        Member member = Member.of(1L);
        String email = "test@test.com";

        // when
        member.changeEmail(email);

        // then
        Assertions.assertThat(member.getEmail()).isEqualTo(email);
    }


    @DisplayName("[bad] 주어진 이메일이 공백인데 이메일을 변경하려고 하면 예외가 발생한다.")
    @Test
    public void changeEmail_exception() {
        // given
        Member member = Member.of(1L);
        String email = "";

        // when & then
        Assertions.assertThatThrownBy(() -> member.changeEmail(email))
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.CHANGE_EMAIL_VALUE_NOT_FOUND.getMessage());
    }


    @DisplayName("[bad] 주어진 이메일이 없는데 이메일을 변경하려고 하면 예외가 발생한다.")
    @Test
    public void changeEmail_exception2() {
        // given
        Member member = Member.of(1L);
        String email = null;

        // when & then
        Assertions.assertThatThrownBy(() -> member.changeEmail(email))
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.CHANGE_EMAIL_VALUE_NOT_FOUND.getMessage());
    }


    @DisplayName("[happy] 주어진 JWT 객체를 사용해서 회원 도메인 내부의 JWT를 변경 시도하면 성공적으로 변경된다.")
    @Test
    public void changeMemberInsideJwt() {
        // given
        Jwt jwtDomain = createJwtDomain();
        Member member = Member.of(1L);

        // when
        member.changeMemberInsideJwt(jwtDomain);

        // then
        Assertions.assertThat(member.getJwt()).isEqualTo(jwtDomain);
    }


    @DisplayName("[bad] 회원 도메인 내부의 JWT가 이미 존재하는데 JWT를 변경하려고 하면 예외가 발생한다.")
    @Test
    public void changeMemberInsideJwt_exception() {
        // given
        Jwt jwtDomain = createJwtDomain();
        Member member = Member.of(1L);
        member.changeMemberInsideJwt(jwtDomain);

        // when & then
        Assertions.assertThatThrownBy(() -> member.changeMemberInsideJwt(jwtDomain))
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.MEMBER_JWT_ALREADY_EXIST.getMessage());
    }


    @DisplayName("[happy] 비밀번호를 변경하면 비밀번호가 변경된다.")
    @Test
    public void changePassword() {
        // given
        Member member = Member.of(1L);
        String password = "changePassword";

        // when
        member.changePassword(password);

        // then
        Assertions.assertThat(member.getPassword()).isEqualTo(password);
    }


    @DisplayName("[happy] 회원 가입시 필수 값이 모두 존재하면 예외가 발생하지 않는다.")
    @Test
    public void validSignUpMemberData() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .password("password")
                .name("test")
                .build();

        // when
        member.validSignUpMemberData();

        // then
        Assertions.assertThat(member).isNotNull();
    }


    @DisplayName("[happy] 회원 가입시 이메일이 없으면 예외가 발생한다.")
    @Test
    public void validSignUpMemberData_exception1() {
        // given
        Member member = Member.builder()
                .password("password")
                .name("test")
                .build();

        // when & then
        Assertions.assertThatThrownBy(member::validSignUpMemberData)
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.MEMBER_REQUIRED_VALUE.getMessage());
    }


    @DisplayName("[happy] 회원 가입시 비밀번호가 없으면 예외가 발생한다.")
    @Test
    public void validSignUpMemberData_exception2() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .name("test")
                .build();

        // when & then
        Assertions.assertThatThrownBy(member::validSignUpMemberData)
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.MEMBER_REQUIRED_VALUE.getMessage());
    }


    @DisplayName("[happy] 회원 가입시 이름이 없으면 예외가 발생한다.")
    @Test
    public void validSignUpMemberData_exception3() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .password("password")
                .build();

        // when & then
        Assertions.assertThatThrownBy(member::validSignUpMemberData)
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.MEMBER_REQUIRED_VALUE.getMessage());
    }


    /**
     * @return JWT 도메인
     * @apiNote test를 위한 JWT 도메인을 생성한다.
     */
    private Jwt createJwtDomain() {
        return Jwt.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

}