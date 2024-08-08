package com.pulse.member.adapter.out.persistence.repository;

import com.pulse.member.adapter.out.persistence.entity.MemberOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberOutboxRepository extends JpaRepository<MemberOutboxEntity, Long> {

    Optional<MemberOutboxEntity> findByPayloadAndEventType(Long id, String eventType);

}
