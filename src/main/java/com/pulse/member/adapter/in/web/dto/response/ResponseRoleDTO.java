package com.pulse.member.adapter.in.web.dto.response;

import lombok.*;

/**
 * 역할 응답 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseRoleDTO {

    private Long id;
    private String name;

}
