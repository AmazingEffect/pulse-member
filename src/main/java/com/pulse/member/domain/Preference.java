package com.pulse.member.domain;

import lombok.*;

/**
 * 사용자 환경 설정 엔티티
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Preference {

    private Long id;
    private Member memberEntity;
    private boolean emailNotifications; // 이메일 알림 설정
    private boolean smsNotifications;   // SMS 알림 설정
    private boolean profilePublic;      // 프로필 공개 여부
    private boolean allowSearch;        // 검색 허용 여부
    private boolean darkMode;           // 다크 모드 설정
    private String fontSize;            // 폰트 크기 설정
    private String language;            // 언어 설정

}
