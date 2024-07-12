package com.pulse.member.controller.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
