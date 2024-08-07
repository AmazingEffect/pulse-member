package com.pulse.member.adapter.in.web.dto.request;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 생성 DTO
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignUpRequestDTO {

    private String email;            // 이메일
    private String password;         // 소셜 로그인 사용자는 NULL일 수 있음
    private String name;             // 이름
    private String nickname;         // 닉네임
    private String profilePictureUrl; // 프로필 사진
    private String introduction;     // 자기소개
    private String phoneNumber;      // 전화번호
    private String address;          // 주소
    private LocalDateTime birthDate; // 생년월일
    private String gender;           // 성별
    private String statusMessage;    // 상태 메시지

}
