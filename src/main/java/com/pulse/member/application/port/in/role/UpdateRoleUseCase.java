package com.pulse.member.application.port.in.role;

import com.pulse.member.domain.Role;

public interface UpdateRoleUseCase {

    Role updateRoleById(Role role);

    Role updateRoleByName(Role role);

}
