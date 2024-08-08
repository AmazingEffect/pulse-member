package com.pulse.member.application.port.in.role;

import com.pulse.member.adapter.in.web.dto.response.RoleResponseDTO;
import com.pulse.member.application.command.role.CreateRoleCommand;

public interface CreateRoleUseCase {

    RoleResponseDTO createRole(CreateRoleCommand createRoleCommand);

}
