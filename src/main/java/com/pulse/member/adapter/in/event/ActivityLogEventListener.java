package com.pulse.member.adapter.in.event;

import com.pulse.member.adapter.out.event.ActivityLogEvent;
import com.pulse.member.adapter.out.persistence.entity.ActivityLogEntity;
import com.pulse.member.adapter.out.persistence.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class ActivityLogEventListener {

    private final ActivityLogRepository activityLogRepository;

    /**
     * 트랜잭션 커밋 전에 발생하는 이벤트를 처리하는 메서드
     * 활동 로그를 저장한다.
     *
     * @param event 활동 로그 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void activityLogCreate(ActivityLogEvent event) {
        log.info("log save - memberId: {}, action: {}", event.getId(), event.getAction());
        ActivityLogEntity activityLogEntity = ActivityLogEntity.of(event.getId(), event.getAction());
        activityLogRepository.save(activityLogEntity);
    }

}
