package com.pulse.member.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * 역할 정보 엔티티 (유저가 여러개의 역할을 가질수도 있으므로 자체 테이블로 분리)
 * ADMIN, MEMBER, MODERATOR, GUEST, VIP
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // factory method
    public static RoleEntity of(String roleName) {
        return RoleEntity.builder()
                .name(roleName)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntity roleEntity)) return false;
        return id != null && Objects.equals(getId(), roleEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}