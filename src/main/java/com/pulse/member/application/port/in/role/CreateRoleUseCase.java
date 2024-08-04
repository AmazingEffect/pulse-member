package com.pulse.member.application.port.in.role;

import com.pulse.member.domain.Role;

import java.util.List;

public interface CreateRoleUseCase {

    Role createRole(Role role);

    List<Role> createRoles(Role dto);

}
