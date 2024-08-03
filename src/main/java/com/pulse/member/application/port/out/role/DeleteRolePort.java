package com.pulse.member.application.port.out.role;

import com.pulse.member.domain.Role;

public interface DeleteRolePort {

    Boolean deleteRoleById(Role role);

    Boolean deleteRoleByName(Role role);

}
