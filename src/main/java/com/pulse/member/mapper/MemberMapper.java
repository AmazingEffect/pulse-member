package com.pulse.member.mapper;

import com.pulse.member.adapter.in.web.dto.request.SignOutRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.application.command.auth.SignOutCommand;
import com.pulse.member.application.command.auth.SignInCommand;
import com.pulse.member.application.command.auth.SignUpCommand;
import com.pulse.member.domain.Member;
import com.pulse.member.grpc.MemberProto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * componentModel="spring"을 통해서 spring container에 Bean으로 등록 해 준다. (외부에서 주입받아서 사용하면 된다.)
 * unmappedTargetPolicy IGNORE 만약, target class에 매핑되지 않는 필드가 있으면, null로 넣게 되고, 따로 report하지 않는다.
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MemberRoleMapper.class} // MemberRoleMapper를 사용한다.
)
public interface MemberMapper {

    MemberProto.MemberRetrieveResponse toProto(MemberResponseDTO memberResponseDTO);

    // 응답 도메인을 회원가입 응답 DTO로 변환
    MemberResponseDTO domainToResponseDTO(Member member);

    // 도메인을 엔티티로 변환
    MemberEntity toEntity(Member member);

    // 로그아웃 요청 DTO를 엔티티로 변환
    Member toDomain(SignOutRequestDTO logoutRequest);

    // MemberEntity 엔티티를 DTO로 변환
    Member toDomain(MemberEntity memberEntity);

    // 로그인 요청 Command를 도메인으로 변환
    Member commandToDomain(SignInCommand signInCommand);

    // 회원가입 요청 Command를 도메인으로 변환
    Member commandToDomain(SignUpCommand signUpCommand);

    // 로그아웃 요청 Command를 도메인으로 변환
    Member commandToDomain(SignOutCommand signOutCommand);

}
