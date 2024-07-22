package com.pulse.member.mapper;


import com.pulse.member.controller.request.MemberRetrieveDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
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

    // Member 엔티티를 DTO로 변환
    MemberSignUpRequestDTO toCreateDto(Member member);

    // 특정 필드만 MemberRetrieveDTO로 변환
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "nickname", target = "nickname")
    @Mapping(source = "profilePictureUrl", target = "profilePictureUrl")
    @Mapping(source = "statusMessage", target = "statusMessage")
    MemberRetrieveDTO toRetrieveDto(Member member);

    // DTO를 엔티티로 변환
    Member toEntity(MemberSignUpRequestDTO signUpRequestDTO);

    // DTO를 엔티티로 변환
    Member toEntity(MemberRetrieveDTO memberRetrieveDTO);

    // gRPC 요청을 위한 매핑
    @Mapping(target = "id", ignore = true) // id는 생략 가능, 자동 생성되는 경우
    MemberSignUpRequestDTO toCreateDto(MemberProto.MemberRequest memberRequest);

    MemberSignUpRequestDTO toRetrieveDto(MemberProto.MemberRequest memberRequest);

    MemberProto.MemberRetrieveResponse toProto(MemberRetrieveDTO memberRetrieveDTO);

    MemberProto.MemberCreateResponse toProto(MemberSignUpRequestDTO memberCreateDTO);

}
