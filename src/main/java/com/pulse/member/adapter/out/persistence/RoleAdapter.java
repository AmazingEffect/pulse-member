package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.repository.RoleRepository;
import com.pulse.member.application.port.out.role.CreateRolePort;
import com.pulse.member.application.port.out.role.DeleteRolePort;
import com.pulse.member.application.port.out.role.FindRolePort;
import com.pulse.member.application.port.out.role.UpdateRolePort;
import com.pulse.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RoleAdapter
 */
@RequiredArgsConstructor
@Component
public class RoleAdapter implements CreateRolePort, FindRolePort, DeleteRolePort, UpdateRolePort {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        return null;
    }

    @Override
    public Boolean deleteRoleById(Role role) {
        return null;
    }

    @Override
    public Boolean deleteRoleByName(Role role) {
        return null;
    }

    @Override
    public Role findRoleById(Role role) {
        return null;
    }

    @Override
    public Role findRoleByName(Role role) {
        return null;
    }

    @Override
    public Role updateRole(Role role) {
        return null;
    }

}
