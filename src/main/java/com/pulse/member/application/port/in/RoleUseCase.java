package com.pulse.member.application.port.in;

import com.pulse.member.adapter.in.web.dto.request.RoleCreateRequestDTO;
import com.pulse.member.adapter.out.persistence.entity.Role;

import java.util.List;

/**
 * 권한 Service
 */
public interface RoleUseCase {

    Role createRole(String roleName);

    List<Role> createRoles(RoleCreateRequestDTO dto);

}
