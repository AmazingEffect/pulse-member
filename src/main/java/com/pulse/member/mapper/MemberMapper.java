package com.pulse.member.mapper;


import com.pulse.member.dto.MemberDTO;
import com.pulse.member.entity.Member;
import com.pulse.member.grpc.MemberProto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * componentModel="spring"을 통해서 spring container에 Bean으로 등록 해 준다. (외부에서 주입받아서 사용하면 된다.)
 * unmappedTargetPolicy IGNORE 만약, target class에 매핑되지 않는 필드가 있으면, null로 넣게 되고, 따로 report하지 않는다.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    MemberDTO toDto(Member member);
    Member toEntity(MemberDTO memberDTO);

    @Mapping(target = "id", ignore = true) // id는 생략 가능, 자동 생성되는 경우
    MemberDTO toDto(MemberProto.MemberRequest memberRequest);
    MemberProto.MemberResponse toProto(MemberDTO memberDTO);
    MemberDTO toDto(MemberProto.MemberResponse memberResponse);

}
