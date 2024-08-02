package com.pulse.member.application;

import com.pulse.event_library.service.OutboxKafkaService;
import org.springframework.stereotype.Service;

/**
 * OutboxKafkaService의 구현체
 */
@Service
public class OutboxKafkaServiceImpl implements OutboxKafkaService {

    @Override
    public void updateOutboxStatus() {

    }

}
