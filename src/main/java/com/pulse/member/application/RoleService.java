package com.pulse.member.application;

import com.pulse.member.application.port.in.role.CreateRoleUseCase;
import com.pulse.member.application.port.in.role.DeleteRoleUseCase;
import com.pulse.member.application.port.in.role.FindRoleUseCase;
import com.pulse.member.application.port.in.role.UpdateRoleUseCase;
import com.pulse.member.application.port.out.role.CreateRolePort;
import com.pulse.member.application.port.out.role.FindRolePort;
import com.pulse.member.application.port.out.role.UpdateRolePort;
import com.pulse.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleService implements CreateRoleUseCase, FindRoleUseCase, UpdateRoleUseCase, DeleteRoleUseCase {

    private final CreateRolePort createRolePort;
    private final FindRolePort findRolePort;
    private final UpdateRolePort updateMemberPort;

    /**
     * 권한 생성
     *
     * @param role 권한 도메인
     * @return 생성된 권한
     */
    @Transactional
    @Override
    public Role createRole(Role role) {
        role.validRole();
        return createRolePort.createRole(role);
    }


    /**
     * 권한 삭제
     *
     * @param role 권한 도메인
     * @return 삭제된 권한
     */
    @Override
    public Role findRoleById(Role role) {
        role.validRole();
        return findRolePort.findRoleById(role);
    }


    /**
     * 권한 수정
     *
     * @param role 권한 도메인
     * @return 수정된 권한
     */
    @Override
    public Role findRoleByName(Role role) {
        role.validRole();
        return findRolePort.findRoleByName(role);
    }


    /**
     * 권한 수정
     *
     * @param role 권한 도메인
     * @return 수정된 권한
     */
    @Transactional
    @Override
    public Role updateRole(Role role) {
        role.validRole();
        return updateMemberPort.updateRole(role);
    }

}
