package com.pulse.member.domain;

import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

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
    private Jwt jwt;                  // JWT Object 객체

    // factory method
    public static Member of(long id) {
        return Member.builder()
                .id(id)
                .build();
    }


    // factory method
    public static Member of(Long id, String email, String name, LocalDateTime lastLogin) {
        return Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .lastLogin(lastLogin)
                .build();
    }


    /**
     * 이메일을 변경합니다.
     *
     * @param email 이메일
     */
    public void changeEmail(String email) {
        if (ObjectUtils.isEmpty(email)) {
            throw new MemberException(ErrorCode.CHANGE_EMAIL_VALUE_NOT_FOUND);
        }
        this.email = email;
    }


    /**
     * JWT 객체를 변경합니다.
     *
     * @param jwt JWT 객체
     */
    public void changeMemberInsideJwt(Jwt jwt) {
        if (!ObjectUtils.isEmpty(this.jwt)) {
            throw new MemberException(ErrorCode.MEMBER_JWT_ALREADY_EXIST);
        }
        this.jwt = jwt;
    }


    /**
     * 비밀번호를 변경합니다.
     *
     * @param encode 암호화된 비밀번호
     */
    public void changePassword(String encode) {
        this.password = encode;
    }


    /**
     * 회원 가입시 필수 값 체크
     */
    public void validSignUpMemberData() {
        // 회원 가입시 필수 값 체크
        if (email == null || password == null || name == null) {
            throw new MemberException(ErrorCode.MEMBER_REQUIRED_VALUE);
        }
    }

}
