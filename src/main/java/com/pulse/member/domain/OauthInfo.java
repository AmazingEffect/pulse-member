package com.pulse.member.domain;

import lombok.*;

/**
 * 소셜 로그인 정보 엔티티
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OauthInfo {

    private Long id;
    private Member memberEntity;
    private String oauthProvider; // 소셜 로그인 제공자 정보 (google, kakao, naver 등)
    private String oauthId; // 소셜 로그인 제공자의 사용자 ID

}
