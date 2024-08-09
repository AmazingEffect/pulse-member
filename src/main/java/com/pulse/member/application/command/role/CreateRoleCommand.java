package com.pulse.member.application.command.role;

import com.pulse.member.adapter.in.web.dto.request.RoleCreateRequestDTO;
import com.pulse.member.adapter.out.persistence.entity.constant.RoleName;
import lombok.*;

/**
 * 권한 생성 Command
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateRoleCommand {

    private RoleName roleName;


    // factory method
    public static CreateRoleCommand of(RoleCreateRequestDTO requestDTO) {
        return CreateRoleCommand.builder()
                .roleName(RoleName.of(requestDTO.getRoleName()))
                .build();
    }

}
