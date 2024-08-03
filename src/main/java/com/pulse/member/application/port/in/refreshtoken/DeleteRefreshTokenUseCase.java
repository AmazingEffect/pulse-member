package com.pulse.member.application.port.in.refreshtoken;

import com.pulse.member.domain.RefreshToken;

public interface DeleteRefreshTokenUseCase {

    Boolean deleteRefreshToken(RefreshToken refreshToken);

}
