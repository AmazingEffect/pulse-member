package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.entity.RoleEntity;
import com.pulse.member.adapter.out.persistence.repository.RoleRepository;
import com.pulse.member.application.port.out.role.CreateRolePort;
import com.pulse.member.application.port.out.role.DeleteRolePort;
import com.pulse.member.application.port.out.role.FindRolePort;
import com.pulse.member.application.port.out.role.UpdateRolePort;
import com.pulse.member.domain.Role;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RoleAdapter
 */
@RequiredArgsConstructor
@Component
public class RolePersistenceAdapter implements CreateRolePort, FindRolePort, DeleteRolePort, UpdateRolePort {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    /**
     * 권한 생성
     *
     * @param roleCode 권한 코드
     * @return 생성된 권한
     */
    @Override
    public Role createRole(String roleCode) {
        RoleEntity roleEntity = RoleEntity.of(roleCode);
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        return roleMapper.toDomain(savedRoleEntity);
    }


    /**
     * 권한 삭제
     *
     * @param role Role
     * @return 삭제 여부
     */
    @Override
    public Boolean deleteRoleById(Role role) {
        return null;
    }


    /**
     * 권한 삭제
     *
     * @param role Role
     * @return 삭제 여부
     */
    @Override
    public Boolean deleteRoleByName(Role role) {
        return null;
    }


    /**
     * 권한 조회
     *
     * @param role Role
     * @return 조회된 권한
     */
    @Override
    public Role findRoleById(Role role) {
        return null;
    }


    /**
     * 권한 조회
     *
     * @param role Role
     * @return 조회된 권한
     */
    @Override
    public Role findRoleByName(Role role) {
        RoleEntity roleEntity = roleRepository.findByName(role.getName())
                .orElseThrow(() -> new MemberException(ErrorCode.INVALID_ROLE_NAME));

        return roleMapper.entityToDTO(roleEntity);
    }


    /**
     * 권한 수정
     *
     * @param role Role
     * @return 수정된 권한
     */
    @Override
    public Role updateRole(Role role) {
        RoleEntity roleEntity = roleMapper.toEntity(role);
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        return roleMapper.toDomain(savedRoleEntity);
    }

}
