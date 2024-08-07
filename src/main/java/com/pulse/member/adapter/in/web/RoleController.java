package com.pulse.member.adapter.in.web;

import com.pulse.member.adapter.in.web.dto.request.RoleCreateRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.ApiResponse;
import com.pulse.member.adapter.in.web.dto.response.ResponseRoleDTO;
import com.pulse.member.application.command.CreateRoleCommand;
import com.pulse.member.application.port.in.role.CreateRoleUseCase;
import com.pulse.member.exception.ErrorCode;
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
        // requestDTO를 command로 변환
        CreateRoleCommand command = CreateRoleCommand.of(requestDTO);

        // useCase는 command를 받아서 responseDTO를 반환
        ResponseRoleDTO responseDTO = createRoleUseCase.createRole(command);

        if (responseDTO == null) ResponseEntity.ok(ApiResponse.fail(ErrorCode.DATA_NOT_FOUND));
        return ApiResponse.success(ResponseEntity.ok(responseDTO));
    }

}
