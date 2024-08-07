package com.pulse.member.adapter.in.web.dto.request;

import com.pulse.member.adapter.out.persistence.entity.constant.RoleName;
import lombok.*;

/**
 * 역할 생성 DTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleCreateRequestDTO {

    private RoleName roleName;

}
