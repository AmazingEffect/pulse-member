package com.pulse.member.adapter.out.persistence;

import com.pulse.member.adapter.out.persistence.repository.RefreshTokenRepository;
import com.pulse.member.application.port.out.refreshtoken.CreateRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.DeleteRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.FindRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.UpdateRefreshTokenPort;
import com.pulse.member.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RefreshTokenAdapter
 */
@RequiredArgsConstructor
@Component
public class RefreshTokenAdapter implements CreateRefreshTokenPort, FindRefreshTokenPort, DeleteRefreshTokenPort, UpdateRefreshTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public RefreshToken createRefreshToken(RefreshToken refreshToken) {
        return null;
    }

    @Override
    public Boolean deleteRefreshToken(RefreshToken refreshToken) {
        return null;
    }

    @Override
    public RefreshToken findRefreshToken(RefreshToken refreshToken) {
        return null;
    }

    @Override
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        return null;
    }

}
