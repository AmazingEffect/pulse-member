package com.pulse.member.adapter.in.web.dto.request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignOutRequestDTO {

    private String email;

}
