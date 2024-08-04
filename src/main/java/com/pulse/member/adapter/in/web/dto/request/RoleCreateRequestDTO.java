package com.pulse.member.adapter.in.web.dto.request;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleCreateRequestDTO {

    private String name;

}
