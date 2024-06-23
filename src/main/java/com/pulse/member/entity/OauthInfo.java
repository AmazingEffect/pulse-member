package com.pulse.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 소셜 로그인 정보 엔티티
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "oauth_info")
public class OauthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "oauth_provider", nullable = false)
    private String oauthProvider; // 소셜 로그인 제공자 정보 (google, kakao, naver 등)

    @Column(name = "oauth_id", nullable = false)
    private String oauthId; // 소셜 로그인 제공자의 사용자 ID

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OauthInfo oauthInfo)) return false;
        return id != null && Objects.equals(getId(), oauthInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
