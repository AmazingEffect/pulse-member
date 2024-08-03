package com.pulse.member.domain;

import lombok.*;

/**
 * 유저와 역할의 Map 테이블
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRole {

    private Long id;
    private Member memberEntity;
    private Role role;

}