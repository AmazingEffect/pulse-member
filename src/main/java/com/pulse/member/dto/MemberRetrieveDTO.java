package com.pulse.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원 조회 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRetrieveDTO {

    private Long id;                 // PK
    private String email;            // 이메일
    private String name;             // 이름
    private String nickname;         // 닉네임
    private String profilePictureUrl; // 프로필 사진
    private String statusMessage;    // 상태 메시지

}
