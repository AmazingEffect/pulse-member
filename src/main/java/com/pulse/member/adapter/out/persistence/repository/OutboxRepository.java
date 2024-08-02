package com.pulse.member.adapter.out.persistence.repository;

import com.pulse.member.adapter.out.persistence.entity.MemberOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OutboxRepository extends JpaRepository<MemberOutbox, Long> {

    Optional<MemberOutbox> findByPayloadAndEventType(Long id, String eventType);

}
