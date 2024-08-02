package com.pulse.member.adapter.out.persistence.repository;

import com.pulse.member.adapter.out.persistence.entity.Member;
import com.pulse.member.adapter.out.persistence.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByMember(Member member);
}
