package com.pulse.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 회원 엔티티
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;               // PK

    @Column(unique = true, nullable = false)
    private String email;          // 이메일

    @Column(name = "password")
    private String password;       // 소셜 로그인 사용자는 NULL일 수 있음

    @Column(name = "name")
    private String name;           // 이름

    @Column(name = "nickname")
    private String nickname;       // 닉네임

    @Column(name = "profile_picture_url")
    private String profilePictureUrl; // 프로필 사진

    @Column(name = "introduction")
    private String introduction;   // 자기소개

    @Column(name = "phone_number")
    private String phoneNumber;    // 전화번호

    @Column(name = "address")
    private String address;        // 주소

    @Column(name = "birth_date")
    private LocalDateTime birthDate; // 생년월일

    @Column(name = "gender")
    private String gender;         // 성별

    @Column(name = "website")
    private String website;        // 웹사이트

    @Column(name = "status_message")
    private String statusMessage;  // 상태 메시지

    @Column(name = "account_status")
    private String accountStatus;  // 계정 상태 (예: 활성화, 비활성화, 정지 등)

    @Column(name = "joined_date")
    private LocalDateTime joinedDate; // 가입일

    @Column(name = "last_login")
    private LocalDateTime lastLogin;  // 마지막 로그인 시간

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return id != null && Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /**
     * 닉네임을 변경하는 메서드
     *
     * @param newNickname 새로운 닉네임
     */
    public void changeNickname(String newNickname) {
        // 추가 로직이나 검증을 여기에 포함할 수 있음
        this.nickname = newNickname;
    }

    /**
     * 프로필 사진 URL을 변경하는 메서드
     *
     * @param newProfilePictureUrl
     */
    public void changeProfilePictureUrl(String newProfilePictureUrl) {
        this.profilePictureUrl = newProfilePictureUrl;
    }

}
