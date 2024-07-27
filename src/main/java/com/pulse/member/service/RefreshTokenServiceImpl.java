package com.pulse.member.service;

import com.pulse.member.controller.response.MemberReadResponseDTO;
import com.pulse.member.entity.RefreshToken;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.repository.RefreshTokenRepository;
import com.pulse.member.service.usecase.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberMapper memberMapper;

    /**
     * RefreshToken 생성
     *
     * @param memberReadResponseDTO 회원 조회 DTO
     * @return 생성된 RefreshToken
     */
    @Transactional
    @Override
    public RefreshToken createRefreshToken(MemberReadResponseDTO memberReadResponseDTO) {
        RefreshToken refreshToken = RefreshToken.builder()
                .member(memberMapper.toEntity(memberReadResponseDTO))
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusMinutes(10))
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
