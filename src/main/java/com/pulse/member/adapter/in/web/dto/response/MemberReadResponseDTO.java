package com.pulse.member.adapter.in.web.dto.response;

import lombok.Data;

@Data
public class MemberReadResponseDTO {

    private Long id;                 // PK
    private String email;            // 이메일
    private String name;             // 이름
    private String nickname;         // 닉네임
    private String profilePictureUrl; // 프로필 사진
    private String statusMessage;    // 상태 메시지

}
