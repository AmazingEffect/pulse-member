package com.pulse.member.domain;

import lombok.*;

/**
 * 유저 역할 정보
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role {

    private Long id;
    private String name;

    public static Role of(String name) {
        return Role.builder()
                .name(name)
                .build();
    }

}