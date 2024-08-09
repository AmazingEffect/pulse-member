package com.pulse.member.domain;

import com.pulse.member.adapter.out.persistence.entity.constant.MessageStatus;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("[Domain] MemberOutbox 단위 테스트")
class MemberOutboxTest {

    @DisplayName("[happy] 회원 아웃박스를 생성하면 생성된다.")
    @Test
    void createMemberOutbox() {
        // given
        MemberOutbox memberOutbox = createMemberOutboxDomain();

        // when
        memberOutbox.changeStatus(MessageStatus.PROCESSED);

        // then
        Assertions.assertThat(memberOutbox.getStatus()).isEqualTo(MessageStatus.PROCESSED);
    }


    @DisplayName("[exception] 주어진 상태값이 없는데 상태값을 변경하려고 하면 예외가 발생한다.")
    @Test
    void changeStatus_exception() {
        // given
        MemberOutbox memberOutbox = createMemberOutboxDomain();

        // when & then
        Assertions.assertThatThrownBy(() -> memberOutbox.changeStatus(null))
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.OUTBOX_STATUS_NOT_FOUND.getMessage());
    }


    @DisplayName("[happy] 주어진 LocalDateTime 객체를 사용해서 회원 아웃박스 내부의 처리시간을 변경 시도하면 성공적으로 변경된다.")
    @Test
    void changeProcessedAt() {
        // given
        MemberOutbox memberOutbox = createMemberOutboxDomain();
        LocalDateTime now = LocalDateTime.now();

        // when
        memberOutbox.changeProcessedAt(now);

        // then
        Assertions.assertThat(memberOutbox.getProcessedAt()).isEqualTo(now);
    }


    @DisplayName("[exception] 주어진 LocalDateTime 객체가 없는데 처리시간을 변경하려고 하면 예외가 발생한다.")
    @Test
    void changeProcessedAt_exception() {
        // given
        MemberOutbox memberOutbox = createMemberOutboxDomain();

        // when & then
        Assertions.assertThatThrownBy(() -> memberOutbox.changeProcessedAt(null))
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorCode.OUTBOX_PROCESSED_AT_NOT_FOUND.getMessage());
    }


    /**
     * @return MemberOutbox
     * @apiNote test를 위한 MemberOutbox 도메인 생성
     */
    private MemberOutbox createMemberOutboxDomain() {
        return MemberOutbox.of("MemberCreatedEvent", 1L, "traceId", MessageStatus.PENDING);
    }

}