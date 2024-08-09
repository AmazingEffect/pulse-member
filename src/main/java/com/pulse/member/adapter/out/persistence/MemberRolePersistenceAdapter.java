package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.entity.MemberRoleEntity;
import com.pulse.member.adapter.out.persistence.entity.RoleEntity;
import com.pulse.member.adapter.out.persistence.repository.MemberRoleRepository;
import com.pulse.member.application.port.out.role.map.CreateMemberRolePort;
import com.pulse.member.domain.Member;
import com.pulse.member.domain.MemberRole;
import com.pulse.member.domain.Role;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.mapper.MemberRoleMapper;
import com.pulse.member.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * MemberRoleAdapter
 */
@RequiredArgsConstructor
@Component
public class MemberRolePersistenceAdapter implements CreateMemberRolePort {

    private final MemberRoleRepository memberRoleRepository;

    private final MemberRoleMapper memberRoleMapper;
    private final MemberMapper memberMapper;
    private final RoleMapper roleMapper;


    /**
     * @param savedMember 저장할 회원
     * @param findRole    저장할 권한
     * @return MemberRole
     * @apiNote MemberRole 생성
     */
    @Override
    public MemberRole createMemberRole(Member savedMember, Role findRole) {
        // 1. 도메인을 엔티티로 변환
        MemberEntity memberEntity = memberMapper.toEntity(savedMember);
        RoleEntity roleEntity = roleMapper.toEntity(findRole);

        // 2. 엔티티 유효성 검사
        validMemberEntity(memberEntity);
        validRoleEntity(roleEntity);

        // 3. 검사완료된 엔티티르 사용하여 MemberRole 엔티티 생성
        MemberRoleEntity memberRoleEntity = MemberRoleEntity.of(memberEntity, roleEntity);

        // 4. MemberRole 엔티티를 저장하고 저장된 엔티티를 도메인으로 변환하여 반환
        return memberRoleMapper.entityToDto(memberRoleRepository.save(memberRoleEntity));
    }


    /**
     * @param memberEntity MemberEntity
     * @apiNote MemberEntity 유효성 검사
     */
    private static void validMemberEntity(MemberEntity memberEntity) {
        if (ObjectUtils.isEmpty(memberEntity)) {
            throw new MemberException(ErrorCode.MEMBER_ENTITY_NOT_FOUND);
        }
    }


    /**
     * @param roleEntity RoleEntity
     * @apiNote RoleEntity 유효성 검사
     */
    private static void validRoleEntity(RoleEntity roleEntity) {
        if (ObjectUtils.isEmpty(roleEntity)) {
            throw new MemberException(ErrorCode.ROLE_ENTITY_NOT_FOUND);
        }
    }

}
