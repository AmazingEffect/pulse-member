package com.pulse.member.application.port.in.refreshtoken;

import com.pulse.member.domain.RefreshToken;

public interface FindRefreshTokenUseCase {

    RefreshToken findRefreshToken(RefreshToken refreshToken);

}
