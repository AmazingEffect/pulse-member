package com.pulse.member.application.command.auth;

import com.pulse.member.adapter.in.web.dto.request.MemberSignUpRequestDTO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원가입 Command
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignUpCommand {

    private String email;            // 이메일
    private String password;         // 소셜 로그인 사용자는 NULL일 수 있음
    private String name;             // 이름
    private String nickname;         // 닉네임
    private String profilePictureUrl; // 프로필 사진
    private String phoneNumber;      // 전화번호
    private LocalDateTime birthDate; // 생년월일
    private String gender;           // 성별
    private String statusMessage;    // 상태 메시지


    // factory method
    public static SignUpCommand of(MemberSignUpRequestDTO requestDTO) {
        return SignUpCommand.builder()
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .name(requestDTO.getName())
                .nickname(requestDTO.getNickname())
                .profilePictureUrl(requestDTO.getProfilePictureUrl())
                .phoneNumber(requestDTO.getPhoneNumber())
                .birthDate(requestDTO.getBirthDate())
                .gender(requestDTO.getGender())
                .statusMessage(requestDTO.getStatusMessage())
                .build();
    }

}
