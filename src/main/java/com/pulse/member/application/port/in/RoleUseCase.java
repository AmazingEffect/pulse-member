package com.pulse.member.application.port.in;

import com.pulse.member.adapter.in.web.dto.request.RoleCreateRequestDTO;
import com.pulse.member.adapter.out.persistence.entity.RoleEntity;

import java.util.List;

/**
 * 권한 Service
 */
public interface RoleUseCase {

    RoleEntity createRole(String roleName);

    List<RoleEntity> createRoles(RoleCreateRequestDTO dto);

}
