package com.pulse.member.application.service;

import com.pulse.member.application.port.in.refreshtoken.CreateRefreshTokenUseCase;
import com.pulse.member.application.port.in.refreshtoken.DeleteRefreshTokenUseCase;
import com.pulse.member.application.port.in.refreshtoken.FindRefreshTokenUseCase;
import com.pulse.member.application.port.in.refreshtoken.UpdateRefreshTokenUseCase;
import com.pulse.member.application.port.out.refreshtoken.CreateRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.DeleteRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.FindRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.UpdateRefreshTokenPort;
import com.pulse.member.domain.Member;
import com.pulse.member.domain.RefreshToken;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JWT 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 주로 RefreshToken을 생성, 조회, 삭제하는 로직을 처리합니다.
 */
@RequiredArgsConstructor
@Service
public class RefreshTokenService implements CreateRefreshTokenUseCase, FindRefreshTokenUseCase, UpdateRefreshTokenUseCase, DeleteRefreshTokenUseCase {

    @Value("${jwt.refreshTokenDurationMinutes}")
    private long refreshTokenDurationMinutes;

    private final CreateRefreshTokenPort createRefreshTokenPort;
    private final FindRefreshTokenPort findRefreshTokenPort;
    private final UpdateRefreshTokenPort updateRefreshTokenPort;
    private final DeleteRefreshTokenPort deleteRefreshTokenPort;


    /**
     * RefreshToken 생성
     *
     * @return 생성된 RefreshToken
     * @apiNote Refresh Token을 단순히 UUID 같은 고유한 식별자로 생성하고, 이를 데이터베이스에 저장하여 관리합니다.
     * 유효기간도 데이터베이스에 함께 저장합니다.
     */
    @Transactional
    @Override
    public RefreshToken createRefreshToken(Member member) {
        RefreshToken refreshToken = RefreshToken.of(member, refreshTokenDurationMinutes);
        return createRefreshTokenPort.createRefreshToken(refreshToken);
    }

    /**
     * RefreshToken 삭제
     *
     * @param member 회원
     */
    @Transactional
    @Override
    public Boolean deleteRefreshToken(Member member) {
        return deleteRefreshTokenPort.deleteRefreshToken(member);
    }

    /**
     * RefreshToken 조회
     *
     * @param refreshToken RefreshToken
     * @return RefreshToken
     */
    @Override
    public RefreshToken findRefreshToken(RefreshToken refreshToken) {
        return findRefreshTokenPort.findRefreshToken(refreshToken);
    }


    /**
     * RefreshToken 업데이트
     *
     * @param refreshToken RefreshToken
     * @return 업데이트된 RefreshToken
     */
    @Transactional
    @Override
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        return null;
    }

}
