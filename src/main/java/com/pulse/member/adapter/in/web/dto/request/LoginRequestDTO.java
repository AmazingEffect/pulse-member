package com.pulse.member.adapter.in.web.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
