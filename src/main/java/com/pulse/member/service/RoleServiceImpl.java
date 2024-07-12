package com.pulse.member.service;

import com.pulse.member.controller.request.RoleCreateRequestDTO;
import com.pulse.member.entity.Role;
import com.pulse.member.repository.RoleRepository;
import com.pulse.member.service.usecase.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public Role createRole(String roleName) {
        return Optional.of(roleRepository.save(Role.of(roleName)))
                .orElseThrow(() -> new IllegalArgumentException("Role 생성에 실패했습니다."));
    }

    @Transactional
    @Override
    public List<Role> createRoles(RoleCreateRequestDTO dto) {
        return roleRepository.saveAll(
                dto.getName().stream()
                        .map(Role::of)
                        .toList()
        );
    }

}
