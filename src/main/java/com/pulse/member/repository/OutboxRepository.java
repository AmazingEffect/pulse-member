package com.pulse.member.repository;

import com.pulse.member.entity.MemberOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OutboxRepository extends JpaRepository<MemberOutbox, Long> {

    Optional<MemberOutbox> findByEventIdAndEventType(Long id, String eventType);

}
