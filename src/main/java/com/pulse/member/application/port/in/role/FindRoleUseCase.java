package com.pulse.member.application.port.in.role;

import com.pulse.member.domain.Role;

public interface FindRoleUseCase {

    Role findRoleById(Role role);

    Role findRoleByName(Role role);

}
