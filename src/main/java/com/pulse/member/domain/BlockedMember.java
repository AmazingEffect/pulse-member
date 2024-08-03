package com.pulse.member.domain;

import lombok.*;

/**
 * 차단된 회원
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockedMember {

    private Long id;
    private Member member;
    private String reason;

}
