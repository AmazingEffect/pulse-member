package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.entity.MemberRoleEntity;
import com.pulse.member.adapter.out.persistence.entity.RoleEntity;
import com.pulse.member.adapter.out.persistence.repository.MemberRoleRepository;
import com.pulse.member.application.port.out.role.map.CreateMemberRolePort;
import com.pulse.member.domain.Member;
import com.pulse.member.domain.MemberRole;
import com.pulse.member.domain.Role;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.mapper.MemberRoleMapper;
import com.pulse.member.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
     * MemberRole 생성
     *
     * @param savedMember 저장할 회원
     * @param findRole    저장할 권한
     * @return MemberRole
     */
    @Override
    public MemberRole createMemberRole(Member savedMember, Role findRole) {
        // 1. 도메인을 엔티티로 변환
        MemberEntity memberEntity = memberMapper.toEntity(savedMember);
        RoleEntity roleEntity = roleMapper.toEntity(findRole);

        // 2. MemberRole 엔티티 생성
        MemberRoleEntity memberRoleEntity = MemberRoleEntity.builder()
                .memberEntity(memberEntity)
                .roleEntity(roleEntity)
                .build();

        // 3. MemberRole 엔티티를 저장하고 저장된 엔티티를 도메인으로 변환하여 반환
        return memberRoleMapper.entityToDto(memberRoleRepository.save(memberRoleEntity));
    }

}
