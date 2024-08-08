package com.pulse.member.adapter.out.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Outbox 테이블에 저장할 kafka topic 이름과 설명을 정의한 enum 클래스
 * kafka의 topic 이름은 코드상으로는 eventType으로 정의되어 있으며, 이벤트 발행 시 사용된다.
 */
@Getter
@AllArgsConstructor
public enum ExternalEventType {

    MEMBER_CREATE_OUTBOX("member-created-outbox", "회원 생성 이벤트"),
    MEMBER_UPDATE_OUTBOX("member-updated-outbox", "회원 수정 이벤트"),
    MEMBER_DELETE_OUTBOX("member-deleted-outbox", "회원 삭제 이벤트"),

    MEMBER_NICKNAME_CHANGE_OUTBOX("member-nickname-change-outbox", "회원 닉네임 변경 이벤트"),
    MEMBER_EMAIL_CHANGE_OUTBOX("member-email-change-outbox", "회원 이메일 변경 이벤트"),
    MEMBER_PROFILE_IMAGE_CHANGE_OUTBOX("member-profile-image-change-outbox", "프로필 이미지 변경 이벤트"),

    ;

    // event type은 kafka topic 이름으로 사용된다.
    private final String type;
    private final String description;

}
