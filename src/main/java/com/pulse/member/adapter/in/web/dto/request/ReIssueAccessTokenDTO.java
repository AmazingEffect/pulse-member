package com.pulse.member.adapter.in.web.dto.request;

import lombok.*;

/**
 * JWT 토큰 재발급 요청 DTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReIssueAccessTokenDTO {

    private String refreshToken; // 리프레시 토큰

}
