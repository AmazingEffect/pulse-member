package com.pulse.member.domain;

import lombok.*;

/**
 * 알림 설정
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationSetting {

    private Long id;
    private Member memberEntity;
    // todo: 친구 신청 추가
    // todo: 추천 기능
    private boolean postLikes;
    private boolean commentLikes;
    private boolean newComments;
    private boolean newShares;

}
