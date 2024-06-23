package com.pulse.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * 역할 정보 엔티티 (유저가 여러개의 역할을 가질수도 있으므로 자체 테이블로 분리)
 * ADMIN, MEMBER, MODERATOR, GUEST, VIP
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return id != null && Objects.equals(getId(), role.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}