package com.pulse.member.mapper;

import com.pulse.member.adapter.in.web.dto.response.RoleResponseDTO;
import com.pulse.member.adapter.out.persistence.entity.RoleEntity;
import com.pulse.member.domain.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    Role entityToDomain(RoleEntity savedRoleEntity);

    RoleEntity toEntity(Role role);

    RoleResponseDTO domainToResponseDTO(Role savedRole);

}
