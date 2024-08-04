package com.pulse.member.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 회원
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    private Long id;               // PK
    private String email;          // 이메일
    private String password;       // 소셜 로그인 사용자는 NULL일 수 있음
    private String name;           // 이름
    private String nickname;       // 닉네임
    private String profilePictureUrl; // 프로필 사진
    private String introduction;   // 자기소개
    private String phoneNumber;    // 전화번호
    private String address;        // 주소
    private LocalDateTime birthDate; // 생년월일
    private String gender;         // 성별
    private String website;        // 웹사이트
    private String statusMessage;  // 상태 메시지
    private String accountStatus;  // 계정 상태 (예: 활성화, 비활성화, 정지 등)
    private LocalDateTime joinedDate; // 가입일
    private LocalDateTime lastLogin;  // 마지막 로그인 시간
    private Set<MemberRole> roles;    // 회원 역할
    private Jwt jwt;                  // JWT Object 객체

    // factory method
    public static Member of(long id) {
        return Member.builder()
                .id(id)
                .build();
    }

    public void changeEmail(String email) {
        this.email = email;
    }


    public void changeJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public void changePassword(String encode) {
        this.password = encode;
    }

}
