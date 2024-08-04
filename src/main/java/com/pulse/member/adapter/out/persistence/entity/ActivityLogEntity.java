package com.pulse.member.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * 활동 로그 엔티티
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "activity_log")
public class ActivityLogEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Column(name = "action")
    private String action;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityLogEntity that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // 로그아웃 생성자
    public static ActivityLogEntity of(Long id, String action) {
        return ActivityLogEntity.builder()
                .memberEntity(MemberEntity.of(id))
                .action(action)
                .build();
    }

}
