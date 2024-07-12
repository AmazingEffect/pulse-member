package com.pulse.member.entity.constant;

import lombok.Getter;

public enum RoleName {

    ADMIN("ADMIN", "관리자"),
    MEMBER("MEMBER", "회원"),
    MODERATOR("MODERATOR", "운영자"),
    GUEST("GUEST", "비회원"),
    VIP("VIP", "VIP");

    @Getter
    private final String roleName;
    private final String description;

    // 역할 이름을 받는 생성자
    RoleName(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

}
