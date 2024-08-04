package com.pulse.member.mapper;

import com.pulse.member.adapter.out.persistence.entity.RoleEntity;
import com.pulse.member.domain.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    Role entityToDto(RoleEntity roleEntity);

    RoleEntity toEntity(Role findRole);

}
