package com.pulse.member.application.port.in.role;

import com.pulse.member.adapter.in.web.dto.response.ResponseRoleDTO;
import com.pulse.member.application.command.CreateRoleCommand;

public interface CreateRoleUseCase {

    ResponseRoleDTO createRole(CreateRoleCommand createRoleCommand);

}
