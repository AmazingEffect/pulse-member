package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.entity.MemberOutboxEntity;
import com.pulse.member.adapter.out.persistence.repository.MemberOutboxRepository;
import com.pulse.member.application.port.out.outbox.CreateMemberOutboxPort;
import com.pulse.member.application.port.out.outbox.FindMemberOutboxPort;
import com.pulse.member.domain.MemberOutbox;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberOutboxMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberOutboxPersistAdapter implements FindMemberOutboxPort, CreateMemberOutboxPort {

    private final MemberOutboxRepository memberOutboxRepository;
    private final MemberOutboxMapper memberOutboxMapper;


    /**
     * @param outbox MemberOutbox
     * @return 저장된 MemberOutbox의 ID
     * @apiNote MemberOutbox 도메인을 저장합니다.
     */
    @Override
    public Long saveMemberOutboxEvent(MemberOutbox outbox) {
        MemberOutboxEntity entity = memberOutboxMapper.domainToEntity(outbox);
        MemberOutboxEntity savedEntity = memberOutboxRepository.save(entity);
        return savedEntity.getId();
    }


    /**
     * @param payload   MemberOutbox의 payload
     * @param eventType MemberOutbox의 eventType
     * @return 조회된 MemberOutbox
     * @apiNote MemberOutbox를 payload와 eventType으로 조회합니다.
     */
    @Override
    public MemberOutbox findMemberOutboxBy(Long payload, String eventType) {
        MemberOutboxEntity entity = memberOutboxRepository.findByPayloadAndEventType(payload, eventType)
                .orElseThrow(() -> new MemberException(ErrorCode.Not_FOUND_MEMBER_OUTBOX));
        return memberOutboxMapper.entityToDomain(entity);
    }

}
