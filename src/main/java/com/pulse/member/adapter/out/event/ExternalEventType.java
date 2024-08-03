package com.pulse.member.adapter.out.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExternalEventType {

    MEMBER_CREATE_OUTBOX("MemberCreatedOutboxEvent", "회원 생성 이벤트"),
    MEMBER_UPDATE_OUTBOX("MemberUpdatedOutboxEvent", "회원 수정 이벤트"),
    MEMBER_DELETE_OUTBOX("MemberDeletedOutboxEvent", "회원 삭제 이벤트"),

    MEMBER_NICKNAME_CHANGE_OUTBOX("MemberNicknameChangeOutboxEvent", "회원 닉네임 변경 이벤트"),
    MEMBER_EMAIL_CHANGE_OUTBOX("MemberEmailChangeOutboxEvent", "회원 이메일 변경 이벤트"),
    MEMBER_PROFILE_IMAGE_CHANGE_OUTBOX("MemberProfileImageChangeOutboxEvent", "프로필 이미지 변경 이벤트"),

    ;

    private final String eventType;
    private final String eventDescription;

}
