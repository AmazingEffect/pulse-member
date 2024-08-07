package com.pulse.member.adapter.in.web.dto.request;

import lombok.*;

/**
 * 회원 조회 DTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberReadRequestDTO {

    private Long id;                 // PK
    private String email;            // 이메일
    private String name;             // 이름
    private String nickname;         // 닉네임
    private String profilePictureUrl; // 프로필 사진
    private String statusMessage;    // 상태 메시지

}
