package com.pulse.member.mapper;

import com.pulse.member.adapter.out.persistence.entity.MemberRoleEntity;
import com.pulse.member.domain.MemberRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MemberRoleMapper {

    @Mapping(target = "member", source = "memberEntity")
    @Mapping(target = "role", source = "roleEntity")
    MemberRole entityToDto(MemberRoleEntity memberRoleEntity);

}
