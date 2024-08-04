package com.pulse.member.adapter.in.web.dto.request;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleCreateRequestDTO {

    private List<String> name;

}
