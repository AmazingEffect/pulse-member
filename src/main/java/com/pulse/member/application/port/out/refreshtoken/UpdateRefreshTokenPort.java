package com.pulse.member.application.port.out.refreshtoken;

import com.pulse.member.domain.RefreshToken;

public interface UpdateRefreshTokenPort {

    RefreshToken updateRefreshToken(RefreshToken refreshToken);

}
