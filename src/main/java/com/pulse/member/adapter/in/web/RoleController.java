package com.pulse.member.adapter.in.web;

import com.pulse.member.adapter.in.web.dto.request.RoleCreateRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.RoleResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.api.ApiResponse;
import com.pulse.member.application.command.role.CreateRoleCommand;
import com.pulse.member.application.port.in.role.CreateRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 역할 관련 API 컨트롤러
 */
@RequestMapping("/member/role")
@RequiredArgsConstructor
@RestController
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;


    /**
     * @param requestDTO RoleCreateRequestDTO
     * @return ResponseEntity<?>
     * @apiNote 권한 생성
     */
    @PostMapping("/create")
    public ApiResponse<ResponseEntity<RoleResponseDTO>> createRole(
            @RequestBody RoleCreateRequestDTO requestDTO
    ) {
        CreateRoleCommand command = CreateRoleCommand.of(requestDTO);
        RoleResponseDTO responseDTO = createRoleUseCase.createRole(command);
        return ApiResponse.success(ResponseEntity.ok(responseDTO));
    }

}
