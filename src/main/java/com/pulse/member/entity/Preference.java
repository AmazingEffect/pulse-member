package com.pulse.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 사용자 환경 설정 엔티티
 * 알림 설정: 이메일 및 SMS 알림 설정
 * 개인정보 설정: 프로필 공개 여부, 검색 허용 여부 등
 * 테마 설정: 다크 모드, 폰트 크기 등
 * 언어 설정: 선호하는 언어 설정
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "preference")
public class Preference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "email_notifications", nullable = false)
    private boolean emailNotifications; // 이메일 알림 설정

    @Column(name = "sms_notifications", nullable = false)
    private boolean smsNotifications;   // SMS 알림 설정

    @Column(name = "profile_public", nullable = false)
    private boolean profilePublic;      // 프로필 공개 여부

    @Column(name = "allow_search", nullable = false)
    private boolean allowSearch;        // 검색 허용 여부

    @Column(name = "dark_mode", nullable = false)
    private boolean darkMode;           // 다크 모드 설정

    @Column(name = "font_size", nullable = false)
    private String fontSize;            // 폰트 크기 설정

    @Column(name = "language", nullable = false)
    private String language;            // 언어 설정

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Preference that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
