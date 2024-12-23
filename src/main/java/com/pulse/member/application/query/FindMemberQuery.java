package com.pulse.member.application.query;

import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import lombok.*;
import org.springframework.util.ObjectUtils;

/**
 * 회원 조회 Command
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FindMemberQuery {

    private Long memberId; // 회원 ID


    // factory method
    public static FindMemberQuery of(Long memberId) {
        // 회원 ID가 없는 경우 예외 처리
        if (ObjectUtils.isEmpty(memberId)) {
            throw new MemberException(ErrorCode.MEMBER_ID_NOT_FOUND);
        }
        return FindMemberQuery.builder()
                .memberId(memberId)
                .build();
    }

}
