package com.pulse.member.application;

import com.pulse.member.adapter.in.web.dto.request.RoleCreateRequestDTO;
import com.pulse.member.adapter.out.persistence.entity.RoleEntity;
import com.pulse.member.adapter.out.persistence.repository.RoleRepository;
import com.pulse.member.application.port.in.RoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleService implements RoleUseCase {

    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public RoleEntity createRole(String roleName) {
        return Optional.of(roleRepository.save(RoleEntity.of(roleName)))
                .orElseThrow(() -> new IllegalArgumentException("RoleEntity 생성에 실패했습니다."));
    }

    @Transactional
    @Override
    public List<RoleEntity> createRoles(RoleCreateRequestDTO dto) {
        return roleRepository.saveAll(
                dto.getName().stream()
                        .map(RoleEntity::of)
                        .toList()
        );
    }

}
