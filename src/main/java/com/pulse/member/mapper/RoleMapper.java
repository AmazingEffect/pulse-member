package com.pulse.member.mapper;

import com.pulse.member.adapter.in.web.dto.request.RoleCreateRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.ResponseRoleDTO;
import com.pulse.member.adapter.out.persistence.entity.RoleEntity;
import com.pulse.member.domain.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    Role entityToDTO(RoleEntity roleEntity);

    RoleEntity toEntity(Role role);

    Role toDomain(RoleCreateRequestDTO requestDTO);

    ResponseRoleDTO toResponseDTO(Role role);

    Role toDomain(RoleEntity roleEntity);

}
