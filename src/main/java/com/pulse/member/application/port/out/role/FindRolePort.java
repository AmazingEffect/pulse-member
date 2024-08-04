package com.pulse.member.application.port.out.role;

import com.pulse.member.domain.Role;

public interface FindRolePort {

    Role findRoleById(Role role);

    Role findRoleByName(Role role);

}
