package com.pulse.member.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * 유저와 역할의 Map 테이블 엔티티
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "member_role")
public class MemberRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity roleEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberRoleEntity that)) return false;
        return id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // factory method
    public static MemberRoleEntity of(MemberEntity memberEntity, RoleEntity roleEntity) {
        return MemberRoleEntity.builder()
                .memberEntity(memberEntity)
                .roleEntity(roleEntity)
                .build();
    }

}