package com.pulse.member.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 알림 설정
 * 사용자가 알림을 설정할 수 있는 기능을 위한 엔티티
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "notification_setting")
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // todo: 친구 신청 추가

    // todo: 추천 기능

    @Column(name = "post_likes", nullable = false)
    private boolean postLikes;

    @Column(name = "comment_likes", nullable = false)
    private boolean commentLikes;

    @Column(name = "new_followers", nullable = false)
    private boolean newComments;

    @Column(name = "new_comments", nullable = false)
    private boolean newShares;

}
