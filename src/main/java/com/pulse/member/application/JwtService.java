package com.pulse.member.application;

import com.pulse.member.adapter.in.web.dto.response.MemberReadResponseDTO;
import com.pulse.member.adapter.out.persistence.entity.RefreshToken;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.adapter.out.persistence.repository.RefreshTokenRepository;
import com.pulse.member.application.port.in.JwtUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JWT 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 주로 RefreshToken을 생성, 조회, 삭제하는 로직을 처리합니다.
 */
@RequiredArgsConstructor
@Service
public class JwtService implements JwtUseCase {

    @Value("${jwt.refreshTokenDurationMinutes}")
    private long refreshTokenDurationMinutes;

    private final MemberMapper memberMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * RefreshToken 생성
     *
     * @param memberReadResponseDTO 회원 조회 DTO
     * @return 생성된 RefreshToken
     * @apiNote Refresh Token을 단순히 UUID 같은 고유한 식별자로 생성하고, 이를 데이터베이스에 저장하여 관리합니다.
     * 유효기간도 데이터베이스에 함께 저장합니다.
     */
    @Transactional
    @Override
    public RefreshToken createRefreshToken(MemberReadResponseDTO memberReadResponseDTO) {
        RefreshToken refreshToken = RefreshToken.builder()
                .member(memberMapper.toEntity(memberReadResponseDTO))
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusMinutes(refreshTokenDurationMinutes))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }


    /**
     * RefreshToken 조회
     *
     * @param token 토큰
     * @return RefreshToken
     */
    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new MemberException(ErrorCode.TOKEN_NOT_FOUND));
    }


    /**
     * RefreshToken 삭제
     *
     * @param memberReadResponseDTO 회원 조회 DTO
     */
    @Transactional
    @Override
    public void deleteByMember(MemberReadResponseDTO memberReadResponseDTO) {
        refreshTokenRepository.deleteByMember(memberMapper.toEntity(memberReadResponseDTO));
    }


    /**
     * RefreshToken 만료 여부 확인
     *
     * @param token RefreshToken
     */
    @Override
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new MemberException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }

}
