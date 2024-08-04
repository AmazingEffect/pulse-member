package com.pulse.member.adapter.in.web;

import com.pulse.member.adapter.in.web.dto.request.RoleCreateRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.ApiResponse;
import com.pulse.member.adapter.in.web.dto.response.ResponseRoleDTO;
import com.pulse.member.application.port.in.role.CreateRoleUseCase;
import com.pulse.member.domain.Role;
import com.pulse.member.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member/role")
@RequiredArgsConstructor
@RestController
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final RoleMapper roleMapper;


    /**
     * 권한 생성
     *
     * @param requestDTO RoleCreateRequestDTO
     * @return ResponseEntity<?>
     */
    @PostMapping("/create")
    public ApiResponse<ResponseEntity<ResponseRoleDTO>> createRole(
            @RequestBody RoleCreateRequestDTO requestDTO
    ) {
        Role role = roleMapper.toDomain(requestDTO);
        Role savedRole = createRoleUseCase.createRole(role);
        ResponseRoleDTO responseRoleDTO = roleMapper.toResponseDTO(savedRole);

        return ApiResponse.success(ResponseEntity.ok(responseRoleDTO));
    }

}
