package com.pulse.member.application.port.in.refreshtoken;

import com.pulse.member.domain.Member;
import com.pulse.member.domain.RefreshToken;

public interface CreateRefreshTokenUseCase {

    RefreshToken createRefreshToken(Member member);

}
