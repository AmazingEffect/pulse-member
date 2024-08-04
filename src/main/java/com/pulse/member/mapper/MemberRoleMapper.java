package com.pulse.member.mapper;

import com.pulse.member.adapter.out.persistence.entity.MemberRoleEntity;
import com.pulse.member.domain.MemberRole;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberRoleMapper {

    MemberRole entityToDto(MemberRoleEntity memberRoleEntity);

}
