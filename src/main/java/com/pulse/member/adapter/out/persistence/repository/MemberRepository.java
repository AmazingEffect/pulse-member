package com.pulse.member.adapter.out.persistence.repository;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByEmail(String email);

    @Query("SELECT m FROM MemberEntity m " +
            "JOIN FETCH m.roles mr " +
            "JOIN FETCH mr.role r " +
            "WHERE m.email = :email")
    Optional<MemberEntity> findByEmailWithRoles(@Param("email") String email);

}
