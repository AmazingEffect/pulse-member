package com.pulse.member.application.port.in.refreshtoken;

import com.pulse.member.domain.Member;

public interface DeleteRefreshTokenUseCase {

    Boolean deleteRefreshToken(Member member);

}
