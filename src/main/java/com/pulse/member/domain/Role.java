package com.pulse.member.domain;

import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import lombok.*;
import org.springframework.util.ObjectUtils;

/**
 * 유저 역할 정보
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role {

    private Long id;
    private String name;

    // factory method
    public static Role of(String name) {
        return Role.builder()
                .name(name)
                .build();
    }


    /**
     * 역할 정보 유효성 검사
     */
    public void validRole() {
        if (ObjectUtils.isEmpty(name)) {
            throw new MemberException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

}