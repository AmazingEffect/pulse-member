package com.pulse.member.application;

import com.pulse.member.application.port.in.role.CreateRoleUseCase;
import com.pulse.member.application.port.in.role.DeleteRoleUseCase;
import com.pulse.member.application.port.in.role.FindRoleUseCase;
import com.pulse.member.application.port.in.role.UpdateRoleUseCase;
import com.pulse.member.application.port.out.role.CreateRolePort;
import com.pulse.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleService implements CreateRoleUseCase, FindRoleUseCase, UpdateRoleUseCase, DeleteRoleUseCase {

    private final CreateRolePort createRolePort;

    @Transactional
    @Override
    public Role createRole(Role role) {
        return createRolePort.createRole(role);
    }

    @Override
    public Role findRoleById(Role role) {
        return null;
    }

    @Override
    public Role findRoleByName(Role role) {
        return null;
    }

}
