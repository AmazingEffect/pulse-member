package com.pulse.member.mapper;

import com.pulse.member.adapter.in.web.dto.response.ResponseRoleDTO;
import com.pulse.member.adapter.out.persistence.entity.RoleEntity;
import com.pulse.member.application.command.CreateRoleCommand;
import com.pulse.member.domain.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    Role entityToDTO(RoleEntity roleEntity);

    RoleEntity toEntity(Role role);

    Role toDomain(RoleEntity roleEntity);

    // command를 도메인으로 변환
    Role commandToDomain(CreateRoleCommand createRoleCommand);

    // 도메인을 responseDTO로 변환
    ResponseRoleDTO domainToResponseDTO(Role savedRole);

}
