package com.pulse.member.application.command.auth;

import com.pulse.member.adapter.in.web.dto.request.SignOutRequestDTO;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignOutCommand {

    private String email;


    // factory method
    public static SignOutCommand of(SignOutRequestDTO signOutRequestDTO) {
        return SignOutCommand.builder()
                .email(signOutRequestDTO.getEmail())
                .build();
    }

}
