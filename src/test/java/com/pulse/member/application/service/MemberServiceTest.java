package com.pulse.member.application.service;

import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.application.port.out.member.CreateMemberPort;
import com.pulse.member.application.port.out.member.DeleteMemberPort;
import com.pulse.member.application.port.out.member.FindMemberPort;
import com.pulse.member.domain.Member;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.mapper.RefreshTokenMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 멤버 서비스 단위 테스트")
class MemberServiceTest {

    @Mock private CreateMemberPort createMemberPort;
    @Mock private FindMemberPort findMemberPort;
    @Mock private DeleteMemberPort deleteMemberPort;
    @Mock private MemberMapper memberMapper;

    @InjectMocks private MemberService sut;

    @DisplayName("[happy] 모든 데이터가 제공되면 회원이 성공적으로 생성된다.")
    @Test
    void createMember() {
        //given
        Member member = Member.builder().id(1L).name("test").email("Test@test.com").password("1234").build();
        MemberResponseDTO responseDTO = MemberResponseDTO.builder().id(1L).name("test").email("Test@test.com").password("1234").build();

        given(createMemberPort.createMember(any(Member.class))).willReturn(member);
        given(memberMapper.domainToResponseDTO(any(Member.class))).willReturn(responseDTO);

        // when
        MemberResponseDTO result = sut.createMember(member);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
    }

    @DisplayName("[bad] 이메일 데이터가 누락되면 회원 생성에 실패한다.")
    @Test
    void createMemberException1() {
        //given
        Member member = Member.builder().id(1L).name("test").password("1234").build();

        // when & then
        Assertions.assertThatThrownBy(() -> sut.createMember(member))
                .isInstanceOf(MemberException.class)
                .hasMessageContaining(ErrorCode.MEMBER_CREATE_EMAIL_NOT_FOUND.getMessage());
    }

    @DisplayName("[bad] 이름 데이터가 누락되면 회원 생성에 실패한다.")
    @Test
    void createMemberException2() {
        //given
        Member member = Member.builder().id(1L).email("test@test.com").password("1234").build();

        // when & then
        Assertions.assertThatThrownBy(() -> sut.createMember(member))
                .isInstanceOf(MemberException.class)
                .hasMessageContaining(ErrorCode.MEMBER_CREATE_NAME_NOT_FOUND.getMessage());
    }

    @DisplayName("[bad] 비밀번호 데이터가 누락되면 회원 생성에 실패한다.")
    @Test
    void createMemberException3() {
        //given
        Member member = Member.builder().id(1L).name("test").email("test@test.com").build();

        // when & then
        Assertions.assertThatThrownBy(() -> sut.createMember(member))
                .isInstanceOf(MemberException.class)
                .hasMessageContaining(ErrorCode.MEMBER_CREATE_PASSWORD_NOT_FOUND.getMessage());
    }

}