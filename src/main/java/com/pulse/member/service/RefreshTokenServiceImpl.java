package com.pulse.member.service;

import com.pulse.member.controller.request.MemberReadRequestDTO;
import com.pulse.member.controller.response.MemberReadResponseDTO;
import com.pulse.member.entity.RefreshToken;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.repository.RefreshTokenRepository;
import com.pulse.member.service.usecase.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.jwtRefreshExpirationMinutes}")
    private Long refreshTokenDurationMinutes;

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberMapper memberMapper;

    @Override
    public RefreshToken createRefreshToken(MemberReadResponseDTO memberReadResponseDTO) {
        RefreshToken refreshToken = RefreshToken.builder()
                .member(memberMapper.toEntity(memberReadResponseDTO))
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusMinutes(refreshTokenDurationMinutes))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }


    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new MemberException(ErrorCode.TOKEN_NOT_FOUND));
    }


    @Override
    public void deleteByMember(MemberReadResponseDTO memberReadResponseDTO) {
        refreshTokenRepository.deleteByMember(memberMapper.toEntity(memberReadResponseDTO));
    }


    @Override
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new MemberException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }

}
