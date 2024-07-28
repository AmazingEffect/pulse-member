package com.pulse.member.listener.spring;

import com.pulse.member.entity.ActivityLog;
import com.pulse.member.listener.spring.event.ActivityLogEvent;
import com.pulse.member.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpringEventListener {

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
        ActivityLog activityLog = ActivityLog.of(event.getId(), event.getAction());
        activityLogRepository.save(activityLog);
    }

}
