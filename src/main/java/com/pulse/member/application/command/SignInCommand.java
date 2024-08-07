package com.pulse.member.application.command;

import com.pulse.member.adapter.in.web.dto.request.SignInRequestDTO;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignInCommand {

    private String email;
    private String password;


    // factory method
    public static SignInCommand of(SignInRequestDTO signInRequestDTO) {
        return SignInCommand.builder()
                .email(signInRequestDTO.getEmail())
                .password(signInRequestDTO.getPassword())
                .build();
    }

}
