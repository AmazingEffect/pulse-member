package com.pulse.member.service.usecase;

import com.pulse.member.controller.request.RoleCreateRequestDTO;
import com.pulse.member.entity.Role;

import java.util.List;

/**
 * 권한 Service
 */
public interface RoleService {

    Role createRole(String roleName);

    List<Role> createRoles(RoleCreateRequestDTO dto);

}
