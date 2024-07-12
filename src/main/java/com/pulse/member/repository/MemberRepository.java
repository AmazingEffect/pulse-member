package com.pulse.member.repository;

import com.pulse.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m " +
            "JOIN FETCH m.roles mr " +
            "JOIN FETCH mr.role r " +
            "WHERE m.email = :email")
    Optional<Member> findByEmailWithRoles(@Param("email") String email);

}
