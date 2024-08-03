package com.pulse.member.domain;

import lombok.*;


/**
 * 활동 로그
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityLog {

    private Long id;
    private String action;

}
