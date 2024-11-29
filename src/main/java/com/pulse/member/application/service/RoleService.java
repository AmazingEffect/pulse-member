package com.pulse.member.application.service;

import com.pulse.member.adapter.in.web.dto.response.RoleResponseDTO;
import com.pulse.member.application.command.role.CreateRoleCommand;
import com.pulse.member.application.port.in.role.CreateRoleUseCase;
import com.pulse.member.application.port.in.role.DeleteRoleUseCase;
import com.pulse.member.application.port.in.role.FindRoleUseCase;
import com.pulse.member.application.port.in.role.UpdateRoleUseCase;
import com.pulse.member.application.port.out.role.CreateRolePort;
import com.pulse.member.application.port.out.role.FindRolePort;
import com.pulse.member.application.port.out.role.UpdateRolePort;
import com.pulse.member.common.annotation.UseCase;
import com.pulse.member.domain.Role;
import com.pulse.member.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@UseCase
public class RoleService implements CreateRoleUseCase, FindRoleUseCase, UpdateRoleUseCase, DeleteRoleUseCase {

    private final CreateRolePort createRolePort;
    private final FindRolePort findRolePort;
    private final UpdateRolePort updateMemberPort;
    private final RoleMapper roleMapper;


    /**
     * @param createRoleCommand 권한 생성 command
     * @return 생성된 권한
     * @apiNote 권한 생성
     */
    @Transactional
    @Override
    public RoleResponseDTO createRole(CreateRoleCommand createRoleCommand) {
        // 1. 권한 생성 및 저장
        Role savedRole = createRolePort.createRole(createRoleCommand.getRoleName().getRoleCode());

        // 2. 저장된 권한을 responseDTO로 변환 후 반환
        return roleMapper.domainToResponseDTO(savedRole);
    }


    /**
     * @param role 권한 도메인
     * @return 조회된 권한
     * @apiNote ID로 권한 찾기
     */
    @Override
    public Role findRoleById(Role role) {
        role.validRole();
        return findRolePort.findRoleById(role);
    }


    /**
     * @param role 권한 도메인
     * @return 조회된 권한
     * @apiNote 이름으로 권한 조회
     */
    @Override
    public Role findRoleByName(Role role) {
        role.validRole();
        return findRolePort.findRoleByName(role);
    }


    /**
     * @param role 권한 도메인
     * @return 수정된 권한
     * @apiNote 권한 수정
     */
    @Transactional
    @Override
    public Role updateRole(Role role) {
        role.validRole();
        return updateMemberPort.updateRole(role);
    }

}
