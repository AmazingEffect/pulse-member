package com.pulse.member.application.command.auth;

import com.pulse.member.adapter.in.web.dto.request.ReIssueAccessTokenDTO;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReIssueAccessTokenCommand {

    private String refreshToken;


    // factory method
    public static ReIssueAccessTokenCommand of(ReIssueAccessTokenDTO request) {
        return ReIssueAccessTokenCommand.builder()
                .refreshToken(request.getRefreshToken())
                .build();
    }

}
