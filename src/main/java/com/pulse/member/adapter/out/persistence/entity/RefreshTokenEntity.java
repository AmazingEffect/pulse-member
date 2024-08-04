package com.pulse.member.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 일반적인 JWT 기반 인증 시스템에서는 처음 로그인할 때 서버는 클라이언트(안드로이드 기기)에 액세스 토큰과 리프레시 토큰을 반환합니다.
 * 동시에, 서버는 리프레시 토큰을 자신의 데이터베이스(RDB)에 저장하여 나중에 유효성 검증 및 토큰 재발급 시 사용할 수 있도록 합니다.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    // expiryDate를 데이터베이스에 저장하는 이유는 여러 가지가 있습니다.
    // 기본적으로 토큰 내부에 만료 시간을 저장할 수 있지만, 데이터베이스에 명시적으로 만료 시간을 저장하면 여러 가지 이점을 제공합니다.
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshTokenEntity that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
