package com.pulse.member.adapter.out.persistence.entity.constant;

import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum RoleName {

    ADMIN("ADMIN", "관리자"),
    MEMBER("MEMBER", "회원"),
    MODERATOR("MODERATOR", "운영자"),
    GUEST("GUEST", "비회원"),
    VIP("VIP", "VIP");

    private final String roleCode;
    private final String description;


    /**
     * 역할명으로 RoleName 찾기
     *
     * @param roleCode 역할명 코드
     * @return RoleName
     */
    public static RoleName of(String roleCode) {
        return Arrays.stream(RoleName.values())
                .filter(role -> role.getRoleCode().equals(roleCode))
                .findFirst()
                .orElseThrow(() -> new MemberException(ErrorCode.INVALID_ROLE_NAME));
    }

}
