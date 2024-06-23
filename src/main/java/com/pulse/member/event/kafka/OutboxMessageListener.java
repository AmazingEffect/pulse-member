package com.pulse.member.event.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 서버간 데이터 전송: Outbox 관련 Kafka 리스너
 * @KafkaListener 어노테이션을 사용하면 동일한 그룹 ID를 사용하더라도 각 리스너는 지정된 토픽에 따라 독립적으로 동작한다. (여러 서버에서 동시 호출)
 * Kafka의 동작 방식에 따라, 동일한 토픽을 구독하지만 다른 groupId를 가진 컨슈머 그룹은 동일한 메시지를 각각의 그룹에서 수신하고 처리합니다.
 */
@Service
public class OutboxMessageListener {

    /**
     * Kafka 외부 리스너
     * 다른 서버에서 데이터 동기화를 위해 Event를 발행하면 이 리스너가 동작해서 Kafka의 메시지를 수신한다.
     */
    @KafkaListener(topics = "ContentCreatedEvent", groupId = "member-group")
    public void listenExternal(String message) {
        System.out.println("Received Message in group member-group (ContentCreatedEvent): " + message);
    }

    /**
     * Kafka 내부 리스너
     * Outbox 테이블에 message_status와 processed_at 컬럼을 업데이트한다.
     */
    @KafkaListener(topics = "MemberCreatedEvent", groupId = "member-group")
    public void listenInternal(String message) {
        System.out.println("Received Message in group member-group (MemberCreatedEvent): " + message);
    }

}
