package com.pulse.member.domain;

import lombok.*;

/**
 * 리프레시 토큰
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

    private Long id;
    private Member memberEntity;
    private String token;

}
